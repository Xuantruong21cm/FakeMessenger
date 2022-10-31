package org.app.admob.applovin

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import org.app.admob.AdMobViewListener
import org.app.admob.AdState
import org.app.admob.R

class NativeViewPreload(
  var context: Context,
  var adUnitId: String = "YOUR_AD_UNIT_ID",
  var listener: AdMobViewListener? = null) {

  private var nativeAdLoader: MaxNativeAdLoader = MaxNativeAdLoader(adUnitId, context)
  private var loadedNativeView: MaxNativeAdView? = null
  private var nativeAd: MaxAd? = null

  init {
    nativeAdLoader.setRevenueListener { listener?.onAdViewStateChanged(AdState.REVENUE_PAID) }
    nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
      override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
        if (nativeAd != null) {
          nativeAdLoader.destroy(nativeAd)
        }

        nativeAd = ad
        loadedNativeView = createNativeAdView()
        val ret = nativeAdLoader.render(loadedNativeView, nativeAd)
        Log.d("NativeADViewPreload", "render result: $ret")
        listener?.onAdViewStateChanged(AdState.LOADED)
      }

      override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
        listener?.onAdViewStateChanged(AdState.LOAD_FAILED)
        Handler(Looper.getMainLooper()).postDelayed({ nativeAdLoader.loadAd() }, 1000)
      }

      override fun onNativeAdClicked(ad: MaxAd) {
        listener?.onAdViewStateChanged(AdState.CLICKED)
      }
    })

    nativeAdLoader.loadAd()
  }

  fun showAds(viewContainer: FrameLayout) {
    loadedNativeView?.apply {
      viewContainer.removeAllViews()
      viewContainer.addView(this)
    }
  }

  fun reloadAds() {
    nativeAdLoader.loadAd()
    loadedNativeView = null
  }

  fun clean() {
    if (nativeAd != null) {
      nativeAdLoader.destroy(nativeAd)
    }

    nativeAdLoader.destroy()
  }

  private fun createNativeAdView(): MaxNativeAdView {
    val binder: MaxNativeAdViewBinder = MaxNativeAdViewBinder.Builder(R.layout.native_ads_custom_view_preload)
      .setTitleTextViewId(R.id.native_ads_preload_title)
      .setBodyTextViewId(R.id.native_ads_preload_body)
      .setAdvertiserTextViewId(R.id.native_ads_preload_advertiser)
      .setIconImageViewId(R.id.native_ads_preload_icon_view)
      .setMediaContentViewGroupId(R.id.native_ads_preload_media_container)
      .setOptionsContentViewGroupId(R.id.native_ads_preload_options_view)
      .setCallToActionButtonId(R.id.native_ads_preload_cta_button)
      .build()

    return MaxNativeAdView(binder, context)
  }
}