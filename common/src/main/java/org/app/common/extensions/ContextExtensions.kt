package org.app.common.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import java.io.IOException
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import java.io.FileNotFoundException

fun Context?.isNetworkConnected(cb: (() -> Unit)? = null): Boolean {
  val cm = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
  var isConnected = cm?.activeNetworkInfo?.isConnected
  if (isConnected == null) isConnected = false
  if (isConnected) cb?.invoke()
  return isConnected
}

fun Context.readStringFromAsset(path: String): String? {
  return try {
    this.assets.open(path).bufferedReader().use { it.readText() }
  } catch (e: IOException) {
    null
  } catch (e: FileNotFoundException) {
    null
  }
}

fun Context.toastMsg(msg: Int) =
  Toast.makeText(this, this.getString(msg), Toast.LENGTH_SHORT).show()

fun Context.toastMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context.getColorR(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.getColorRAsHex(@ColorRes colorRes: Int) =
    String.format("#%06x", ContextCompat.getColor(this, colorRes) and 0xffffff)

fun Context.color(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

fun Context.dimenInt(@DimenRes dimen: Int) = resources.getDimensionPixelSize(dimen)

fun Context.dimenFloat(@DimenRes dimen: Int) = resources.getDimension(dimen)

fun View.dimenInt(@DimenRes dimen: Int) = resources.getDimensionPixelSize(dimen)

fun Context.getDrawableR(@DrawableRes drawable: Int) = ContextCompat.getDrawable(this, drawable)

fun Context.getDrawable(drawableName: String) =
    resources.getIdentifier(drawableName, "drawable", packageName)

fun Context.startActivity(applyIntent: Intent.() -> Unit) {
    startActivity(Intent().apply { applyIntent() })
}

fun Context.quantityString(@PluralsRes res: Int, number: Int) =
    resources.getQuantityString(res, number)

fun Context.getStringArray(@ArrayRes res: Int) = resources.getStringArray(res)

fun Context.getIntArray(@ArrayRes res: Int) = resources.getIntArray(res)

fun Context.dpToPixel(dp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

fun Context.openStore() {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

val Context.screenSize:Pair<Int,Int>
    get() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.let { it.width() to it.height() }
        } else {
            val displayMetrics = DisplayMetrics()
            val display = windowManager.defaultDisplay
            display.getRealMetrics(displayMetrics)
            displayMetrics.heightPixels to displayMetrics.widthPixels
        }

    }