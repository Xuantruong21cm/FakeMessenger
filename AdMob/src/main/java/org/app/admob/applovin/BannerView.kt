package org.app.admob.applovin

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdkUtils
import org.app.admob.AdMobViewListener
import org.app.admob.AdState.*

class BannerView private constructor(builder: Builder) {
  private var viewContainer: FrameLayout? = builder.container
  private var context: Context = builder.context

  private var adUnitId: String
  private var bgColor: Int = Color.WHITE
  private lateinit var adView: MaxAdView

  private var listener: AdMobViewListener?

  init {
    adUnitId = builder.adUnitId
    bgColor = builder.bgColor
    listener = builder.listener
    setupViews()
  }

  private fun setupViews() {
    adView = MaxAdView(adUnitId, context)

    adView.setListener(object : MaxAdViewAdListener {
      override fun onAdLoaded(ad: MaxAd?) {
        listener?.onAdViewStateChanged(LOADED)
      }

      override fun onAdDisplayed(ad: MaxAd?) {
        listener?.onAdViewStateChanged(DISPLAYED)
      }

      override fun onAdHidden(ad: MaxAd?) {
        listener?.onAdViewStateChanged(HIDDEN)
      }

      override fun onAdClicked(ad: MaxAd?) {
        listener?.onAdViewStateChanged(CLICKED)
      }

      override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
        listener?.onAdViewStateChanged(LOAD_FAILED)
        Handler(Looper.getMainLooper()).postDelayed({ adView.loadAd() }, 1000)
      }

      override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
        listener?.onAdViewStateChanged(DISPLAY_FAILED)
        adView.loadAd()
      }

      override fun onAdExpanded(ad: MaxAd?) {
        listener?.onAdViewStateChanged(EXPANDED)
      }

      override fun onAdCollapsed(ad: MaxAd?) {
        listener?.onAdViewStateChanged(COLLAPSED)
      }
    })
    adView.setRevenueListener { listener?.onAdViewStateChanged(REVENUE_PAID) }

    val isTablet = AppLovinSdkUtils.isTablet(context)
    val heightPx = AppLovinSdkUtils.dpToPx(context, if (isTablet) 90 else 50)

    adView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx)
    adView.setBackgroundColor(bgColor)
//    adView.startAutoRefresh()
    viewContainer?.addView(adView)

    adView.loadAd()
  }

  fun setAutoRefresh(isAuto: Boolean) {
    if (isAuto) {
      adView.startAutoRefresh()
    } else {
      adView.stopAutoRefresh()
    }
  }

  fun destroyAd() {
    adView.stopAutoRefresh()
    adView.destroy()
  }

  class Builder(internal var context: Context,
                internal var container: FrameLayout? = null) {

    internal var adUnitId = "YOUR_AD_UNIT_ID"

    internal var bgColor: Int = Color.WHITE

    internal var listener: AdMobViewListener? = null

    fun setAdUnitId(adUnitId: String): Builder {
      this.adUnitId = adUnitId
      return this
    }

    fun setBgColor(color: Int): Builder {
      this.bgColor = color
      return this
    }

    fun setListener(listener: AdMobViewListener): Builder {
      this.listener = listener
      return this
    }

    fun build(): BannerView {
      return BannerView(this)
    }
  }
}