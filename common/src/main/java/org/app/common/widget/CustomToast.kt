package org.app.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import org.app.common.R

class CustomToast {
  companion object {
    @SuppressLint("InflateParams")
    fun makeText(
      context: Context?,
      contentToast: String,
      @DrawableRes imgRes: Int,
      paddingBottom: Int? = null
    ): Toast {
      val toast = Toast(context)
      val layout =
        LayoutInflater.from(context).inflate(R.layout.custom_toast, null, false)
      val content = layout.findViewById<TextView>(R.id.tvContent)
      val imgToast = layout.findViewById<AppCompatImageView>(R.id.imgToast)
      var paddingBottom = paddingBottom
      if (paddingBottom == null) paddingBottom =
        context?.resources?.getDimension(R.dimen.dimen26)?.toInt() ?: 0
      layout.setPadding(0, 0, 0, paddingBottom)
      content?.text = contentToast
      imgToast.setImageResource(imgRes)
      toast.view = layout
      toast.duration = Toast.LENGTH_SHORT
      toast.setGravity(
        Gravity.FILL_HORIZONTAL or Gravity.BOTTOM,
        0,
        0
      )
      return toast
    }

    @SuppressLint("InflateParams")
    fun makeText(
      context: Context?,
      @StringRes resContent: Int,
      @DrawableRes imgRes: Int,
      paddingBottom: Int? = null,
      gravity: Int? = null
    ): Toast {
      val toast = Toast(context)
      val layout =
        LayoutInflater.from(context).inflate(R.layout.custom_toast, null, false)
      val content = layout.findViewById<TextView>(R.id.tvContent)
      val imgToast = layout.findViewById<AppCompatImageView>(R.id.imgToast)
      var paddingBottom = paddingBottom
      if (paddingBottom == null) paddingBottom =
        context?.resources?.getDimension(R.dimen.dimen16)?.toInt() ?: 0
      layout.setPadding(0, 0, 0, paddingBottom)
      content?.text = context?.getString(resContent)
      imgToast.setImageResource(imgRes)
      toast.view = layout
      toast.duration = Toast.LENGTH_SHORT
      toast.setGravity(
        Gravity.FILL_HORIZONTAL or (gravity ?: Gravity.BOTTOM),
        0,
        0
      )
      return toast
    }
  }
}