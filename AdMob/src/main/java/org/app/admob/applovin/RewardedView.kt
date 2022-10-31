package org.app.admob.applovin

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd
import org.app.admob.AdMobViewListener
import org.app.admob.AdState.*

class RewardedView private constructor(builder: Builder) {
  private var activity: Activity = builder.activity

  private var adUnitId: String
  private var listener: AdMobViewListener?
  private var rewardedAd: MaxRewardedAd
  private var retryAttempt = 0.0

  init {
    adUnitId = builder.adUnitId
    listener = builder.listener
    rewardedAd = MaxRewardedAd.getInstance(adUnitId, activity)
    setupListener()
  }

  private fun setupListener() {
    rewardedAd.setListener(object : MaxRewardedAdListener {
      override fun onAdLoaded(ad: MaxAd?) {
        retryAttempt = 0.0
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
        if (retryAttempt < 3) {
          retryAttempt++
          Handler(Looper.getMainLooper()).postDelayed({ rewardedAd.loadAd() }, 500)
        } else {
          retryAttempt = 0.0
          listener?.onAdViewStateChanged(LOAD_FAILED)
        }
      }

      override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
        listener?.onAdViewStateChanged(DISPLAY_FAILED)
        rewardedAd.loadAd()
      }

      override fun onRewardedVideoStarted(ad: MaxAd?) {
        listener?.onAdViewStateChanged(REWARD_VIDEO_STARTED)
      }

      override fun onRewardedVideoCompleted(ad: MaxAd?) {
        listener?.onAdViewStateChanged(REWARD_VIDEO_COMPLETED)
      }

      override fun onUserRewarded(ad: MaxAd?, reward: MaxReward?) {
        listener?.onAdViewStateChanged(USER_REWARDED)
      }

    })
    rewardedAd.setRevenueListener { listener?.onAdViewStateChanged(REVENUE_PAID) }

    rewardedAd.loadAd()
  }

  fun reloadAd() {
    rewardedAd.loadAd()
  }

  fun showAd(): Boolean {
    if (rewardedAd.isReady) {
      rewardedAd.showAd()
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

    fun build(): RewardedView {
      return RewardedView(this)
    }
  }
}