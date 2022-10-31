package org.app.admob.applovin

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import org.app.admob.AdMobViewListener
import org.app.admob.AdState

class InterstitialView private constructor(builder: Builder) {
  private var activity: Activity = builder.activity

  private var listener: AdMobViewListener?
  private var interstitialAd: MaxInterstitialAd
  private var retryAttempt = 0.0

  init {
    listener = builder.listener
    interstitialAd = MaxInterstitialAd(builder.adUnitId, activity)
    setupListener()
  }

  private fun setupListener() {
    interstitialAd.setListener(object : MaxAdListener {
      override fun onAdLoaded(ad: MaxAd?) {
        retryAttempt = 0.0
        listener?.onAdViewStateChanged(AdState.LOADED)
      }

      override fun onAdDisplayed(ad: MaxAd?) {
        listener?.onAdViewStateChanged(AdState.DISPLAYED)
      }

      override fun onAdHidden(ad: MaxAd?) {
        listener?.onAdViewStateChanged(AdState.HIDDEN)
      }

      override fun onAdClicked(ad: MaxAd?) {
        listener?.onAdViewStateChanged(AdState.CLICKED)
      }

      override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
        if (retryAttempt < 3) {
          retryAttempt++
          Handler(Looper.getMainLooper()).postDelayed({ interstitialAd.loadAd() }, 500)
        } else {
          retryAttempt = 0.0
          listener?.onAdViewStateChanged(AdState.LOAD_FAILED)
        }
      }

      override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
        if (retryAttempt < 3) {
          retryAttempt++
          Handler(Looper.getMainLooper()).postDelayed({ interstitialAd.loadAd() }, 500)
        } else {
          retryAttempt = 0.0
          listener?.onAdViewStateChanged(AdState.DISPLAY_FAILED)
        }
      }
    })

    interstitialAd.setRevenueListener { listener?.onAdViewStateChanged(AdState.REVENUE_PAID) }
    interstitialAd.loadAd()
  }

  fun destroyAd() {
    interstitialAd.destroy()
  }

  fun reloadAd() {
    interstitialAd.loadAd()
  }

  fun isAdReady() = interstitialAd.isReady

  fun showAd(): Boolean {
    if (interstitialAd.isReady) {
      interstitialAd.showAd()
      return true
    }
    return false
  }

  class Builder(internal var activity: Activity) {

    internal var adUnitId = "YOUR_AD_UNIT_ID"

    internal var listener: AdMobViewListener? = null

    fun setAdUnitId(adUnitId: String): Builder {
      this.adUnitId = adUnitId
      return this
    }

    fun setListener(listener: AdMobViewListener): Builder {
      this.listener = listener
      return this
    }

    fun build(): InterstitialView {
      return InterstitialView(this)
    }
  }

}