package com.merryblue.fakemessenger.data.general.remote

import com.merryblue.fakemessenger.data.general.remote.response.OtherAppResponse
import org.app.data.model.BaseResponse
import retrofit2.http.*

interface ManagerServices {

    @GET("api/v1/apps/list")
    suspend fun  getAppList(): BaseResponse<List<OtherAppResponse>>

    @POST("api/v1/feedback/create")
    suspend fun sendFeedback(
        @Query("app_id") app_id: String,
        @Query("content_feedback") content_feedback: String,
    ): BaseResponse<String>

    @FormUrlEncoded
    @POST("rating/create")
    suspend fun createRating(
        @Field("app_id") applicationId: String,
        @Field("number_rate") numberRate: Int
    ): BaseResponse<Any>
}