package com.merryblue.fakemessenger.ui.splash

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import org.app.common.BaseViewModel
import javax.inject.Inject
import com.merryblue.fakemessenger.data.account.repository.HomeRepository


@HiltViewModel
class SplashViewModel @Inject constructor(private val homeRepository: HomeRepository,
                                          application: Application
) : BaseViewModel(application) {

    fun isFirstTime() = homeRepository.isFirstLaunch
}