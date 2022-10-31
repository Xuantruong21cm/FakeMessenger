package com.merryblue.fakemessenger.data.general.repository

import com.merryblue.fakemessenger.data.model.OtherAppModel
import org.app.data.model.BaseResponse

interface ManagerRepository {
    suspend fun getAppList() : BaseResponse<List<OtherAppModel>>

    suspend fun sendFeedback(appId: String, feedback: String)

    suspend fun sendRating(appId: String, rate: Int)
}