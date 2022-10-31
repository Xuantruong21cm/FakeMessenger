package org.app.admob.applovin

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import org.app.admob.AdMobViewListener
import org.app.admob.AdState
import java.util.*

class AppOpenAdView(private var adUnitId: String) {
    private val TAG = "AppOpenAdView"
    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    var isShowingAd = false
    private var loadTime: Long = 0

    fun loadAd(context: Context) {
        if (isLoadingAd || isAdAvailable()) {
            return
        }

        isLoadingAd = true
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            adUnitId,
            request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                    Log.d(TAG, "onAdLoaded.")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isLoadingAd = false
                    Log.d(TAG, "onAdFailedToLoad: " + loadAdError.message)
                }
            }
        )
    }

    /** Check if ad was loaded more than n hours ago. */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    /** Check if ad exists and can be shown. */
    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    fun showAdIfAvailable(activity: Activity) {
        showAdIfAvailable(
            activity,
            object : AdMobViewListener {
                override fun onAdViewStateChanged(state: AdState) {
                    // Do nothing
                }

                override fun onShowAdOpenAppComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }

            }
        )
    }

    fun showAdIfAvailable(activity: Activity, listener: AdMobViewListener) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
            Log.i(TAG, "The app open ad is already showing.")
            return
        }

        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (!isAdAvailable()) {
            Log.i(TAG, "The app open ad is not ready yet.")
            listener.onShowAdOpenAppComplete()
            loadAd(activity)
            return
        }

        Log.d(TAG, "Will show ad.")

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            /** Called when full screen content is dismissed. */
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isShowingAd = false
                Log.d(TAG, "onAdDismissedFullScreenContent.")

                listener.onShowAdOpenAppComplete()
                loadAd(activity)
            }

            /** Called when fullscreen content failed to show. */
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                appOpenAd = null
                isShowingAd = false
                Log.d(TAG, "onAdFailedToShowFullScreenContent: " + adError.message)

                listener.onShowAdOpenAppComplete()
                loadAd(activity)
            }

            /** Called when fullscreen content is shown. */
            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "onAdShowedFullScreenContent.")
            }
        }
        isShowingAd = true
        appOpenAd?.show(activity)
    }
}