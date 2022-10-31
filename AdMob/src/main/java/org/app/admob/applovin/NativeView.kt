package org.app.admob.applovin

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import org.app.admob.AdMobViewListener
import org.app.admob.AdState.*

class NativeView private constructor(builder: Builder) {
  private var context: Context = builder.context

  @LayoutRes
  private val layoutId = builder.layoutId

  @IdRes
  private var titleTextViewId: Int = 0

  @IdRes
  private var bodyTextViewId: Int = 0

  @IdRes
  private var advertiserTextViewId: Int = 0

  @IdRes
  private var iconImageViewId: Int = 0

  @IdRes
  private var mediaViewId: Int = 0

  @IdRes
  private var optionViewId: Int = 0

  @IdRes
  private var ctaButtonId: Int = 0

  private var viewContainer: FrameLayout? = null
  private var adUnitId: String
  private var bgColor: Int = Color.WHITE
  private lateinit var nativeAdLoader: MaxNativeAdLoader
  private lateinit var loadedNativeView: MaxNativeAdView
  private lateinit var nativeAdLayout: FrameLayout
  private var nativeAd: MaxAd? = null

  private var listener: AdMobViewListener?

  init {
    viewContainer = builder.container
    adUnitId = builder.adUnitId
    bgColor = builder.bgColor
    listener = builder.listener
    titleTextViewId = builder.titleTextViewId
    bodyTextViewId = builder.bodyTextViewId
    advertiserTextViewId = builder.advertiserTextViewId
    iconImageViewId = builder.iconImageViewId
    mediaViewId = builder.mediaViewId
    optionViewId = builder.optionViewId
    ctaButtonId = builder.ctaButtonId
    setupViews()
  }

  private fun setupViews() {
    nativeAdLayout = requireNotNull(viewContainer)

    val binder: MaxNativeAdViewBinder = MaxNativeAdViewBinder.Builder(layoutId)
      .setTitleTextViewId(titleTextViewId)
      .setBodyTextViewId(bodyTextViewId)
      .setAdvertiserTextViewId(advertiserTextViewId)
      .setIconImageViewId(iconImageViewId)
      .setMediaContentViewGroupId(mediaViewId)
      .setOptionsContentViewGroupId(optionViewId)
      .setCallToActionButtonId(ctaButtonId)
      .build()
    loadedNativeView = MaxNativeAdView(binder, context)

    nativeAdLoader = MaxNativeAdLoader(adUnitId, context)
    nativeAdLoader.setRevenueListener { listener?.onAdViewStateChanged(REVENUE_PAID) }
    nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
      override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
        listener?.onAdViewStateChanged(LOADED)
        if (nativeAd != null) {
          nativeAdLoader.destroy(nativeAd)
        }

        nativeAd = ad

        nativeAdLayout.removeAllViews()
        val ret = nativeAdLoader.render(loadedNativeView, nativeAd)
        nativeAdLayout.addView(loadedNativeView)
        Log.d("NativeADView", "render result: $ret")
      }

      override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
        listener?.onAdViewStateChanged(LOAD_FAILED)
        Handler(Looper.getMainLooper()).postDelayed({ nativeAdLoader.loadAd() }, 1000)
      }

      override fun onNativeAdClicked(ad: MaxAd) {
        listener?.onAdViewStateChanged(CLICKED)
      }
    })

    nativeAdLoader.loadAd()
  }

  fun reloadAd() {
    nativeAdLoader.loadAd()
  }

  fun clean() {
    if (nativeAd != null) {
      nativeAdLoader.destroy(nativeAd)
    }

    nativeAdLoader.destroy()
  }

  class Builder(
    internal var context: Context,
    @LayoutRes internal var layoutId: Int) {

    internal var adUnitId = "YOUR_AD_UNIT_ID"

    internal var bgColor: Int = Color.WHITE

    internal var listener: AdMobViewListener? = null

    internal var container: FrameLayout? = null

    @IdRes
    internal var titleTextViewId: Int = 0

    @IdRes
    internal var bodyTextViewId: Int = 0

    @IdRes
    internal var advertiserTextViewId: Int = 0

    @IdRes
    internal var iconImageViewId: Int = 0

    @IdRes
    internal var mediaViewId: Int = 0

    @IdRes
    internal var optionViewId: Int = 0

    @IdRes
    internal var ctaButtonId: Int = 0

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

    fun setContainer(container: FrameLayout): Builder {
      this.container = container
      return this
    }

    fun setTitleTextViewId(@IdRes id: Int): Builder {
      this.titleTextViewId = id
      return this
    }

    fun setBodyTextViewId(@IdRes id: Int): Builder {
      this.bodyTextViewId = id
      return this
    }

    fun setAdvertiserTextViewId(@IdRes id: Int): Builder {
      this.advertiserTextViewId = id
      return this
    }

    fun setIconImageViewId(@IdRes id: Int): Builder {
      this.iconImageViewId = id
      return this
    }

    fun setMediaViewId(@IdRes id: Int): Builder {
      this.mediaViewId = id
      return this
    }

    fun setOptionViewId(@IdRes id: Int): Builder {
      this.optionViewId = id
      return this
    }

    fun setCtaButtonId(@IdRes id: Int): Builder {
      this.ctaButtonId = id
      return this
    }

    fun build(): NativeView {
      return NativeView(this)
    }
  }
}