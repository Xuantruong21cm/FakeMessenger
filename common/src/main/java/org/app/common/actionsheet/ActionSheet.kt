package org.app.common.actionsheet

data class ActionSheet(var type: Int, var name: String, var code: String? = null, var isSelected: Boolean = false)