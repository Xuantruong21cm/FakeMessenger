package com.merryblue.fakemessenger.ui.policy

import androidx.fragment.app.viewModels
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.FragmentPolicyLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import org.app.common.BaseFragment

@AndroidEntryPoint
class PolicyFragment : BaseFragment<FragmentPolicyLayoutBinding>() {

    private val viewModel: PolicyViewModel by viewModels()

    override
    fun getLayoutId() = R.layout.fragment_policy_layout

    override
    fun setBindingVariables() {
        binding.viewModel = viewModel
    }

    override
    fun setUpViews() {

    }
    
}