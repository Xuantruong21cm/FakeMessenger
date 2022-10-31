package com.merryblue.fakemessenger.data.general.remote.response

import com.google.gson.annotations.SerializedName

data class OtherAppResponse(

    @field:SerializedName("category_name")
    val categoryName: String? = null,

    @field:SerializedName("name_project")
    val nameProject: String? = null,

    @field:SerializedName("time_update_store")
    val timeUpdateStore: String? = null,

    @field:SerializedName("icon")
    val icon: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("name_in_store")
    val nameInStore: String? = null,

    @field:SerializedName("size")
    val size: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("link_android")
    val linkAndroid: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("company")
    val company: String? = null,

    @field:SerializedName("link_ios")
    val linkIos: Any? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("app_id")
    val appId: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)
