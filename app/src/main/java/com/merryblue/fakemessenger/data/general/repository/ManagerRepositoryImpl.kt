package com.merryblue.fakemessenger.data.general.repository

import android.content.Context
import com.merryblue.fakemessenger.data.general.remote.ManagerRemoteDataSource
import com.merryblue.fakemessenger.data.model.OtherAppModel
import org.app.data.model.BaseResponse
import org.app.data.model.ResponseData
import javax.inject.Inject

class ManagerRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dataSource: ManagerRemoteDataSource
) : ManagerRepository {

    override suspend fun getAppList() : BaseResponse<List<OtherAppModel>> {

        return when(val response = dataSource.getOtherApps()) {
            is ResponseData.Success -> {
                if(response.value.result != null && response.value.result!!.isNotEmpty()) {
                    val models = response.value.result!!.map { OtherAppModel(it) }
                    val packageName = context.packageName
                    val filtered = models.filter { !packageName.equals(it.id)  }

                    BaseResponse(status = true, result = filtered)
                } else {
                    BaseResponse(status = true, result = listOf())
                }
            }

            is ResponseData.Failure -> BaseResponse(status = false, message = response.message)

            else -> { BaseResponse(status = false) }
        }
    }

    override suspend fun sendFeedback(appId: String, feedback: String) {
        TODO("Not yet implemented")
    }

    override suspend fun sendRating(appId: String, rate: Int) {
        TODO("Not yet implemented")
    }

}