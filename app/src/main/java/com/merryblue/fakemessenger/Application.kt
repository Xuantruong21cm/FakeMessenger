package com.merryblue.fakemessenger

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.multidex.MultiDex
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import dagger.hilt.android.HiltAndroidApp
import org.app.admob.AdMobSdk
import org.app.common.BaseApplication
import org.app.common.utils.InAppPurchase
import org.app.common.utils.TimberDebugTree
import timber.log.Timber
import java.security.KeyManagementException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

@HiltAndroidApp
class Application : BaseApplication() {

  override
  fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)

    MultiDex.install(this)
  }

  override
  fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(TimberDebugTree())
    }

    // Log event active application
    FacebookSdk.sdkInitialize(applicationContext);
    AppEventsLogger.activateApp(this);

    AdMobSdk.initializeSdk(this) {
      Log.d("Application", "initializeSdk completed!!!")
    }

    InAppPurchase.initialize(this)
    updateAndroidSecurityProvider()
//    printOutHash()
  }

  private fun updateAndroidSecurityProvider() {
    // To fix the following issue, when run app in cellular data, Apis not working
    // javax.net.ssl.SSLHandshakeException: SSL handshake aborted: ssl=0x7edfc49e08: I/O error during system call, Connection reset by peer
    try {
      ProviderInstaller.installIfNeeded(applicationContext)
      val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
      sslContext.init(null, null, null)
      sslContext.createSSLEngine()
    } catch (e: GooglePlayServicesRepairableException) {
      e.printStackTrace()
    } catch (e: GooglePlayServicesNotAvailableException) {
      e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
    } catch (e: KeyManagementException) {
      e.printStackTrace()
    }
  }

  private fun printOutHash() {
    try {
      val info: PackageInfo = packageManager.getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES)
      for (signature in info.signatures) {
        val md: MessageDigest = MessageDigest.getInstance("SHA")
        md.update(signature.toByteArray())
        Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
      }
    } catch (e: PackageManager.NameNotFoundException) {
      Log.d("KeyHash e1", e.localizedMessage.toString() + "")
    } catch (e: NoSuchAlgorithmException) {
      Log.d("KeyHash e2", e.localizedMessage + "")
    }
  }
}