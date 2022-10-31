package org.app.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import org.app.common.R
import java.nio.charset.StandardCharsets
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@SuppressLint("HardwareIds")
fun getDeviceSerialNumber(context: Context): String? {
  // We're using the Reflection API because Build.SERIAL is only available
  // since API Level 9 (Gingerbread, Android 2.3).
  return try {
    val deviceSerial = Build::class.java.getField("SERIAL")[null] as String
    if (TextUtils.isEmpty(deviceSerial) || Build.UNKNOWN == deviceSerial) {
      Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
      )
    } else {
      deviceSerial
    }
  } catch (ignored: Exception) {
    // Fall back  to Android_ID
    Settings.Secure.getString(
      context.contentResolver,
      Settings.Secure.ANDROID_ID
    )
  }
}

var trustAllCerts = arrayOf<TrustManager>(
  object : X509TrustManager {
    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
      return arrayOf()
    }
  }
)

fun showSoftKeyboard(activity: Activity, editText: EditText?) {
  val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun showSoftKeyboard(activity: Activity) {
  val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}


fun hideSoftKeyboard(activity: Activity?) {
  if (activity == null) {
    return
  }
  val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  val v = activity.currentFocus
  if (imm != null && v != null) {
    imm.hideSoftInputFromWindow(v.windowToken, 0)
  }
}

fun checkNetwork(activity: Context, text: String?): Int {
  val manager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
    ?.isConnectedOrConnecting ?: false
  val isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    ?.isConnectedOrConnecting ?: false
  if (isWifi) return ConnectivityManager.TYPE_WIFI
  if (is3g) return ConnectivityManager.TYPE_MOBILE
  if (!is3g && !isWifi) {
    Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    return -1
  }
  return -1
}

fun getDialogWaiting(context: Context?): Dialog {
  val dialogLoad = Dialog(context!!)
  dialogLoad.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
  dialogLoad.setContentView(R.layout.dialog_post_loading)
  dialogLoad.setCanceledOnTouchOutside(false)
  dialogLoad.setCancelable(false)
  return dialogLoad
}

@Throws(Exception::class)
fun <T> addDataArrayFromAssetFileJson(
  context: Context, filename: String?,
  type: Class<Array<T>>?
): List<T>? {
  //Read file Json from Asset
  val `is` = context.assets.open(filename!!)
  val size = `is`.available()
  val buffer = ByteArray(size)
  `is`.read(buffer)
  `is`.close()
  val json = String(buffer, StandardCharsets.UTF_8)
  //Return list data with Generic T
  val data: MutableList<T> = ArrayList()
  val gson = Gson()
  val arrayClass = gson.fromJson(json, type)
  for (element in arrayClass) {
    data.add(element)
  }
  return data
}

fun getSSLSocketFactory(): SSLSocketFactory? {
  // Install the all-trusting trust manager
  var sslContext: SSLContext? = null
  try {
    sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, SecureRandom())
  } catch (e: NoSuchAlgorithmException) {
    e.printStackTrace()
  } catch (e: KeyManagementException) {
    e.printStackTrace()
  }
  var sslSocketFactory: SSLSocketFactory? = null
  // Create an ssl socket factory with our all-trusting manager
  if (sslContext != null) sslSocketFactory = sslContext.socketFactory
  return sslSocketFactory
}

fun hideKeyword(view: View, activity: Activity?) {
  // Set up touch listener for non-text box views to hide keyboard.
  if (view !is EditText) {
    view.setOnTouchListener { v, event ->
      hideSoftKeyboard(activity)
      false
    }
  }

  //If a layout container, iterate over children and seed recursion.
  if (view is ViewGroup) {
    for (i in 0 until view.childCount) {
      val innerView = view.getChildAt(i)
      hideKeyword(innerView, activity)
    }
  }
}