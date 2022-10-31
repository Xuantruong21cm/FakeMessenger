package com.merryblue.fakemessenger.data.account.repository


import org.app.data.local.AppPreferences
import org.app.data.model.LanguageModel
import javax.inject.Inject

interface HomeRepository {

  var isFirstOpenApp: Boolean

  var isFirstLaunch: Boolean

  fun clearPreferences()

  fun getUserLanguage(): LanguageModel?

  fun setUserLanguage(data: LanguageModel)
}

class HomeRepositoryImpl @Inject constructor(
  private val appPreferences: AppPreferences
) : HomeRepository {

  override var isFirstOpenApp: Boolean
    get() = appPreferences.isFirstOpenApp()
    set(value) {
      appPreferences.setFirstOpenApp(value)
    }

  override var isFirstLaunch: Boolean
    get() = appPreferences.isFirstTime
    set(value) {
      appPreferences.isFirstTime = value
    }

  override
  fun clearPreferences() = appPreferences.clearPreferences()

  override fun getUserLanguage() = appPreferences.currentLanguageModel

  override fun setUserLanguage(data: LanguageModel) {
    appPreferences.currentLanguageModel = data
  }
}