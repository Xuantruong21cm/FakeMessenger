package com.merryblue.fakemessenger.data.model

import com.merryblue.fakemessenger.data.general.remote.response.OtherAppResponse

data class OtherAppModel(
    val categoryName: String,
    val name: String,
    val icon: String,
    val description: String,
    val size: String,
    val id: String? = null,
) {
    constructor(data: OtherAppResponse) : this(
        data.categoryName ?: "",
        data.nameProject ?: "",
        data.icon ?: "",
        data.description ?: "",
        data.size ?: "",
        data.appId
    )
}