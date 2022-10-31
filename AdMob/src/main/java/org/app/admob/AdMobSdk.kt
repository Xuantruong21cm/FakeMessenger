package org.app.admob

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import com.applovin.sdk.AppLovinSdk
import com.google.android.gms.ads.MobileAds
import org.app.admob.applovin.AppOpenAdView
import org.app.admob.applovin.NativeViewPreload
import java.util.*

@SuppressLint("StaticFieldLeak")
object AdMobSdk {
  private const val TAG = "AdMobSdk"
  private var context: Context? = null
  private var appOpenAdView: AppOpenAdView? = null
  private var adsViewMap: MutableMap<String, Queue<Any>> = HashMap()
  private var sizeMap: MutableMap<String, Int> = HashMap()

  fun initializeSdk(context: Context,
                    callback: (() -> Unit)? = null) {
    this.context = context
    // Make sure to set the mediation provider value to "max" to ensure proper functionality
//    MobileAds.initialize(context) {}
    AppLovinSdk.getInstance(context).mediationProvider = "max"
    AppLovinSdk.getInstance(context).initializeSdk {
      Log.d(TAG, "initializeSdk complete!!!")
      callback?.invoke()
    }
  }

  fun preloadNativeAdView(unitId: String,
                          size: Int) {
    context?.let {
      sizeMap[unitId] = size
      var queue = adsViewMap[unitId]
      for (i in 0 until size) {
        val nativeAdsView = NativeViewPreload(it, unitId)
        if (queue == null) {
          queue = LinkedList()
        }
        queue.add(nativeAdsView)
      }

      adsViewMap[unitId] = queue!!
    }
  }

  fun getNativeAdView(unitId: String) : NativeViewPreload? {
    val queue = adsViewMap[unitId]
    val nativeAdsView = if (context != null) NativeViewPreload(context!!, unitId) else null
    return if (queue != null) {
      val adview = queue.poll()
      nativeAdsView?.let { queue.add(nativeAdsView) }
      adsViewMap[unitId] = queue

      adview as? NativeViewPreload
    } else {
      nativeAdsView
    }
  }

  fun showDebugView(context: Context) {
    AppLovinSdk.getInstance(context).showMediationDebugger()
  }

  // AdsUnitId for testing
  fun preloadAppOpenAd(appOpenAdsId: String = "ca-app-pub-3940256099942544/3419835294") {
    appOpenAdView = AppOpenAdView(appOpenAdsId)
  }

  fun showAppOpenAdIfAvailable(activity: Activity, listener: AdMobViewListener) {
    appOpenAdView?.showAdIfAvailable(activity, listener)
  }

  fun isShowingAppOpenAd(): Boolean {
    return appOpenAdView?.isShowingAd ?: false
  }
}