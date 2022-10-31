package com.merryblue.fakemessenger.ui.moreapp

import android.app.Application
import com.merryblue.fakemessenger.data.general.repository.ManagerRepository
import com.merryblue.fakemessenger.data.model.OtherAppModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.app.common.BaseViewModel
import org.app.common.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class MoreAppViewModel @Inject constructor(
    application: Application, private val repository: ManagerRepository
) : BaseViewModel(application) {

    val appListEvent by lazy { SingleLiveEvent<List<OtherAppModel>>() }

    fun getOtherApps() {
        launchDataLoadWithoutProgress {
            val response = repository.getAppList()
            if (response.status == true && !response.result.isNullOrEmpty()) {
                appListEvent.postValue(response.result!!)
            } else {
                appListEvent.postValue(listOf())
            }
        }
    }
}