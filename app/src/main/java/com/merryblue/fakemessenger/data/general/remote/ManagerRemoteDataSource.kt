package com.merryblue.fakemessenger.data.general.remote

import org.app.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class ManagerRemoteDataSource @Inject constructor(private val apiService: ManagerServices) :
    BaseRemoteDataSource() {

    suspend fun getOtherApps() = safeApiCall {
        apiService.getAppList()
    }

    suspend fun createFeedBack(applicationId: String, feedbackContent: String) = safeApiCall {
        apiService.sendFeedback(applicationId, feedbackContent)
    }

    suspend fun createRating(applicationId: String, numberRate: Int) = safeApiCall {
        apiService.createRating(applicationId, numberRate)
    }
}