package com.merryblue.fakemessenger.ui.home

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import org.app.common.BaseViewModel
import javax.inject.Inject
import com.merryblue.fakemessenger.data.account.repository.HomeRepository

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application
) : BaseViewModel(application) {

}
