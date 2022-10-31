package com.merryblue.fakemessenger.enum

import android.content.Context
import com.merryblue.fakemessenger.R

enum class HomeTab(val value: Int) {
    LINK(0),
    BROWSER(1),
    DOWNLOADED(2);

    fun getTitle(context: Context?) : String {
        context?.let {
            return when (this) {
                LINK -> { context.getString(R.string.txt_insert_link) }
                BROWSER -> { context.getString(R.string.txt_home) }
                DOWNLOADED -> { context.getString(R.string.txt_download) }
            }
        } ?: kotlin.run {
            return ""
        }
    }
}