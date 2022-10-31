package org.app.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @field:SerializedName("status")
    val status: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("results")
    val result: T? = null,
)