package org.app.common

import android.os.Bundle
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope


/**
 * Created by Hoang Dep Trai on 05/23/2022.
 */

interface BaseAndroidComponent<VB : ViewBinding> {

    val binding: VB

    var safetyScope: CoroutineScope?

    val needToSubscribeEventBus: Boolean

    fun getViewBinding(parent: ViewGroup? = null): VB

    fun init(savedInstanceState: Bundle?) {
        binding.initView(savedInstanceState)
        binding.setupViewModel()
        binding.initListener()
    }

    fun VB.initView(savedInstanceState: Bundle?) {}

    fun VB.initListener() {}

    fun VB.setupViewModel() {}

    fun onBackPress() = false

    fun updateContentHeightAds(height: Int) {
    }

}