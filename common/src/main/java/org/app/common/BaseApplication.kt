package org.app.common

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import com.zeugmasolutions.localehelper.LocaleAwareApplication
import org.app.common.utils.NetworkUtil
import timber.log.Timber
import java.lang.ref.WeakReference

abstract class BaseApplication : LocaleAwareApplication() {

  companion object {
    private lateinit var sInstance: BaseApplication
    private var isAppLaunching = true

    @JvmStatic
    fun getInstance(): BaseApplication {
      return sInstance
    }

    @JvmStatic
    fun checkNetwork(): Boolean {
      return NetworkUtil.isNetworkAvailable(getInstance())
    }

    @JvmStatic
    fun getStringResource(@StringRes res: Int): String {
      return sInstance.getString(res)
    }
  }

  private var data: MutableMap<String, WeakReference<Any>>? = HashMap()

  override fun onCreate() {
    super.onCreate()
    sInstance = this
  }

  fun getAppContext(): Context {
    return sInstance.applicationContext
  }

  fun setData(id: String, `object`: Any) {
    data!![id] = WeakReference(`object`)
    Timber.d("SIZE: ", "" + data!!.size)
  }

  fun getData(id: String): Any? {
    if (data != null) {
      val objectWeakReference = data!![id]
      if (objectWeakReference != null) {
        return objectWeakReference.get()
      }
    } else {
      return null
    }
    return null
  }

  fun isAppLaunching(): Boolean = isAppLaunching

  fun setIsAppLaunching(value: Boolean) {
    isAppLaunching = value
  }
}