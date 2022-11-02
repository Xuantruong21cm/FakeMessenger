package org.app.common.extensions

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.dimToPixel(@DimenRes dimenRes: Int): Int {
    return resources.getDimensionPixelSize(dimenRes)
}

fun Activity.dipToPix(dpInFloat: Float): Float {
    val scale = resources.displayMetrics.density
    return dpInFloat * scale + 0.5f
}

fun Fragment.dipToPix(dpInFloat: Float): Float {
    val scale = resources.displayMetrics.density
    return dpInFloat * scale + 0.5f
}

fun Fragment.spToPix(sp : Float) : Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

fun dipToPix(dpInFloat: Float, context: Context) : Float {
    val scale = context.resources.displayMetrics.density
    return dpInFloat * scale + 0.5f
}