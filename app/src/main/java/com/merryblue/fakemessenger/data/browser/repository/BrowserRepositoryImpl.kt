package com.merryblue.fakemessenger.data.browser.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import org.app.common.extensions.readStringFromAsset
import org.app.common.extensions.runIO
import javax.inject.Inject

class BrowserRepositoryImpl @Inject constructor(private val context: Context) : BrowserRepository {
    companion object {
        private const val VIDEO_HANDLE_JS_ASSET = "js/facebook_video_handling.js"
    }

    private val _jsCodeVideoHandling by lazy { MutableLiveData<String>() }

    override val jsCodeVideoHandling
        get() = _jsCodeVideoHandling

    override suspend fun loadJSCodeVideoHandling() = runIO {
        if (!_jsCodeVideoHandling.value.isNullOrEmpty()) return@runIO
        _jsCodeVideoHandling.postValue(
            "javascript:${context.readStringFromAsset(VIDEO_HANDLE_JS_ASSET) ?: return@runIO}"
        )
    }
}