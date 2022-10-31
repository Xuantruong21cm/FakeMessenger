package org.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import org.app.data.model.LanguageModel
import javax.inject.Inject

class AppPreferences @Inject constructor(context: Context) {

  companion object {
    private const val APP_PREFERENCES_NAME = "APP-NAME-Cache"
    private const val SESSION_PREFERENCES_NAME = "APP-NAME-UserCache"
    private const val MODE = Context.MODE_PRIVATE
    private const val KEY_FIRST_OPEN_APP = "key_first_open_app"

    private val KEY_CURRENT_LANGUAGE = "user_current_language"
    private val FIRST_TIME = Pair("FIRST_TIME", true)
    private val FIREBASE_TOKEN = Pair("FIREBASE_TOKEN", "")
  }

  private val appPreferences: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES_NAME, MODE)
  private val sessionPreferences: SharedPreferences = context.getSharedPreferences(SESSION_PREFERENCES_NAME, MODE)
  private val gson = GsonBuilder().create()

  private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
  }

  var isFirstTime: Boolean
    get() {
      return appPreferences.getBoolean(FIRST_TIME.first, FIRST_TIME.second)
    }
    set(value) = appPreferences.edit {
      it.putBoolean(FIRST_TIME.first, value)
    }

  var firebaseToken: String?
    get() {
      return sessionPreferences.getString(FIREBASE_TOKEN.first, FIREBASE_TOKEN.second)
    }
    set(value) = sessionPreferences.edit {
      it.putString(FIREBASE_TOKEN.first, value)
    }

  var currentLanguageModel: LanguageModel?
    get() = getObject(KEY_CURRENT_LANGUAGE, LanguageModel::class.java)
    set(value) = putObject(KEY_CURRENT_LANGUAGE, value)

  fun clearPreferences() {
    sessionPreferences.edit {
      it.clear().apply()
    }
  }

  // Template for get/set object
  private fun <T> putObject(key: String?, value: T) {
    val editor: SharedPreferences.Editor = appPreferences.edit()
    editor.putString(key, gson.toJson(value))
    editor.apply()
  }

  private fun <T> getObject(key: String, clazz: Class<T>?): T? {
    val value = appPreferences.getString(key, null)
    return if (value != null) {
      try {
        return gson.fromJson(value, clazz)
      } catch (ex: Exception) {
        ex.printStackTrace()
        return null
      }
    } else {
      null
    }
  }

  fun setFirstOpenApp(isShown: Boolean) {
    val editor: SharedPreferences.Editor = appPreferences.edit()
    editor.also {
      it.putBoolean(KEY_FIRST_OPEN_APP, isShown)
      it.commit()
    }
  }

  fun isFirstOpenApp(): Boolean {
    return appPreferences.getBoolean(KEY_FIRST_OPEN_APP, false)
  }
}