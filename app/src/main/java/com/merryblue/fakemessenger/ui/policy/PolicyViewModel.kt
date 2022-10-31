package com.merryblue.fakemessenger.ui.policy

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import org.app.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class PolicyViewModel @Inject constructor(
    application: Application
) : BaseViewModel(application) {

}