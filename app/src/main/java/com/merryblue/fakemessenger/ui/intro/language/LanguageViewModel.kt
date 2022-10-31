package com.merryblue.fakemessenger.ui.intro.language

import android.app.Application
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import com.merryblue.fakemessenger.data.account.repository.HomeRepository
import com.merryblue.fakemessenger.enum.SupportedLanguage
import org.app.common.BaseViewModel
import org.app.common.utils.SingleLiveEvent
import org.app.data.model.LanguageModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(application: Application,
                                            @ApplicationContext private val context: Context,
                                            private val homeRepository: HomeRepository
) : BaseViewModel(application) {

    val openIntro = SingleLiveEvent<Void>()

    fun getLanguage() : List<LanguageModel> {
        val result = mutableListOf<LanguageModel>()
        val language = homeRepository.getUserLanguage()
        val systemLanguage = Locale.getDefault().language
        var currentLanguage = "en"
        if (language != null) {
            currentLanguage = language.value
        } else if (systemLanguage.isNotBlank()) {
            currentLanguage = systemLanguage
        }

        val selectedModel: SupportedLanguage = try {
            SupportedLanguage.valueOf(currentLanguage.uppercase())
        } catch (ex: Exception) {
            ex.printStackTrace()
            SupportedLanguage.EN
        }

        // Add remaining items
        result.add(selectedModel.toModel(context, true))
        SupportedLanguage.values().forEach { item ->
            if (item != selectedModel) {
                result.add(item.toModel(context))
            }
        }

        return result
    }

    fun updateUserLanguage(language: LanguageModel) = homeRepository.setUserLanguage(language)
}