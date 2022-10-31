package org.app.admob.applovin

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.FrameLayout
import com.applovin.mediation.*
import com.applovin.mediation.ads.MaxAdView
import org.app.admob.AdMobViewListener
import org.app.admob.AdState.*

class MrecView private constructor(builder: Builder) {
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
    adView = MaxAdView(adUnitId, MaxAdFormat.MREC, context)

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
        Handler(Looper.getMainLooper()).postDelayed({ adView.loadAd() }, 1000)
      }

      override fun onAdExpanded(ad: MaxAd?) {
        listener?.onAdViewStateChanged(REVENUE_PAID)
      }

      override fun onAdCollapsed(ad: MaxAd?) {
        listener?.onAdViewStateChanged(COLLAPSED)
      }

    })
    adView.setRevenueListener { listener?.onAdViewStateChanged(REVENUE_PAID) }

    adView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    // Need to set the background or background color for MRECs to be fully functional.
    adView.setBackgroundColor(bgColor)
    viewContainer?.addView(adView)

    adView.setExtraParameter( "allow_pause_auto_refresh_immediately", "true" )
    adView.loadAd()
    adView.startAutoRefresh()
  }

  fun loadAdsManual() {
    adView.stopAutoRefresh()
    adView.loadAd()
  }

  fun setAutoRefresh(auto: Boolean) {
    if (auto) adView.startAutoRefresh() else adView.stopAutoRefresh()
  }

  class Builder(internal var context: Context, internal var container: FrameLayout? = null) {

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

    fun build(): MrecView {
      return MrecView(this)
    }
  }
}