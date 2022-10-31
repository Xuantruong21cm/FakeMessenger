package org.app.common.extensions

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import org.app.common.utils.showMessage

fun <A : Activity> Activity.openActivityAndClearStack(activity: Class<A>) {
  Intent(this, activity).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(this)
    finish()
  }
}

fun <A : Activity> Activity.openActivity(activity: Class<A>) {
  Intent(this, activity).apply {
    startActivity(this)
  }
}

fun Activity.showMessage(message: String?) {
  Handler(Looper.getMainLooper()).post {
    showMessage(this, message)
  }
}