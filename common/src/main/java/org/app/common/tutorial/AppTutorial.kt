package org.app.common.tutorial

import android.graphics.drawable.Drawable
import androidx.annotation.RawRes

data class AppTutorial(var title: String, var content: String, var image: Drawable, @RawRes var animId: Int = 0, var animTitle: String = "")