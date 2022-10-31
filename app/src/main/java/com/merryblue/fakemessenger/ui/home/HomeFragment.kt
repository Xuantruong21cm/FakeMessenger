package com.merryblue.fakemessenger.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import org.app.common.BaseFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getLayoutId() = R.layout.fragment_home
}