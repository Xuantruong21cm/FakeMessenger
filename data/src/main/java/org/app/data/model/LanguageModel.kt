package org.app.data.model

data class LanguageModel(
    var language: String,
    var isSelected: Boolean = false,
    var value: String,
    var flags: Int
)
