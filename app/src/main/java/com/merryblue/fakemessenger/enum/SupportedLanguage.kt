package com.merryblue.fakemessenger.enum

import android.content.Context
import org.app.data.model.LanguageModel
import com.merryblue.fakemessenger.R

enum class SupportedLanguage(val value: String) {
    EN("en"),
    ZH("zh"),
    JA("ja"),
    VI("vi"),
    ES("es"),
    PT("pt"),
    RU("ru"),
    KO("ko"),
    FR("fr"),
    DE("de");

    fun toModel(context: Context, isSelected: Boolean = false) : LanguageModel {
        when(this) {
            EN -> return LanguageModel(context.getString(R.string.txt_language_en), isSelected, "en", R.drawable.flag_en)
            ZH -> return LanguageModel(context.getString(R.string.txt_language_chi), isSelected, "zh", R.drawable.flag_cn)
            JA -> return LanguageModel(context.getString(R.string.txt_language_ja), isSelected, "ja", R.drawable.flag_jp)
            VI -> return LanguageModel(context.getString(R.string.txt_language_vi), isSelected, "vi", R.drawable.flag_vi)
            ES -> return LanguageModel(context.getString(R.string.txt_language_spanish), isSelected, "es", R.drawable.flag_sp)
            PT -> return LanguageModel(context.getString(R.string.txt_language_portuguese), isSelected, "pt", R.drawable.flag_ft)
            RU -> return LanguageModel(context.getString(R.string.txt_language_russiane), isSelected, "ru", R.drawable.flag_rs)
            KO -> return LanguageModel(context.getString(R.string.txt_language_korean), isSelected, "ko", R.drawable.flag_kr)
            FR -> return LanguageModel(context.getString(R.string.txt_language_french), isSelected, "fr", R.drawable.flag_fr)
            DE -> LanguageModel(context.getString(R.string.txt_language_german), isSelected, "de", R.drawable.flag_gr)
        }

        return return LanguageModel(context.getString(R.string.txt_language_en), false, "en", R.drawable.flag_en)
    }
}