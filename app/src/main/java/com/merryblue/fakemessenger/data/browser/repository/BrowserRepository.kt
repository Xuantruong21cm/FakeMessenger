package com.merryblue.fakemessenger.data.browser.repository

import androidx.lifecycle.LiveData

interface BrowserRepository {
    val jsCodeVideoHandling: LiveData<String>

    suspend fun loadJSCodeVideoHandling()
}