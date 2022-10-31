package org.app.admob.applovin

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.adPlacer.MaxAdPlacer
import com.applovin.mediation.nativeAds.adPlacer.MaxAdPlacerSettings
import com.applovin.mediation.nativeAds.adPlacer.MaxRecyclerAdapter
import org.app.admob.AdMobViewListener
import org.app.admob.AdState.*

class NativeRecycler private constructor(builder: Builder) {
  private var activity: Activity = builder.activity

  private var listener: AdMobViewListener?
  var adAdapter: MaxRecyclerAdapter

  init {
    listener = builder.listener

    val settings = MaxAdPlacerSettings(builder.adUnitId)
    settings.addFixedPosition(builder.fixedPosition)
    settings.repeatingInterval = builder.repeatInterval

    adAdapter = MaxRecyclerAdapter(settings, builder.adapter, activity)
    adAdapter.setListener(object : MaxAdPlacer.Listener {
      override fun onAdLoaded(position: Int) {
        listener?.onAdViewStateChanged(LOADED)
      }

      override fun onAdRemoved(position: Int) {
        listener?.onAdViewStateChanged(REMOVED)
      }

      override fun onAdClicked(ad: MaxAd?) {
        listener?.onAdViewStateChanged(CLICKED)
      }

      override fun onAdRevenuePaid(ad: MaxAd?) {
        listener?.onAdViewStateChanged(REVENUE_PAID)
      }
    })

    adAdapter.loadAds()
  }

  fun getMaxAdapter() = adAdapter

  fun loadAd() {
    adAdapter.loadAds()
  }

  fun clean() {
    adAdapter.destroy()
  }

  class Builder(internal var activity: Activity) {

    internal lateinit var adapter: Adapter<*>

    internal var adUnitId = "YOUR_AD_UNIT_ID"

    internal var listener: AdMobViewListener? = null

    internal var fixedPosition = 5

    internal var repeatInterval = 5

    fun setAdapter(adapter: Adapter<*>): Builder {
      this.adapter = adapter
      return this
    }

    fun setAdUnitId(adUnitId: String): Builder {
      this.adUnitId = adUnitId
      return this
    }

    fun setFixedPosition(pos: Int): Builder {
      this.fixedPosition = pos
      return this
    }

    fun setRepeatInterval(repeat: Int): Builder {
      this.repeatInterval = repeat
      return this
    }

    fun setListener(listener: AdMobViewListener): Builder {
      this.listener = listener
      return this
    }

    fun build(): NativeRecycler {
      return NativeRecycler(this)
    }
  }
}