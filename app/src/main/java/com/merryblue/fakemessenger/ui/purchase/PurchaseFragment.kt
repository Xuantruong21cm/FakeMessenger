package com.merryblue.fakemessenger.ui.purchase

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.FragmentPurchaseBinding
import com.merryblue.fakemessenger.ui.intro.language.LanguageFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import org.app.common.BaseFragment
import org.app.common.extensions.navigateSafe

@AndroidEntryPoint
class PurchaseFragment : BaseFragment<FragmentPurchaseBinding>() {
    override fun getLayoutId() = R.layout.fragment_purchase

    private val viewModel: PurchaseViewModel by viewModels()
    lateinit var adapter: PurchaseAdapter

    override fun initView(view: View) {
        showLoading("Loading...")
    }

    override
    fun setBindingVariables() {
        binding.viewModel = viewModel
    }

    override
    fun setUpViews() {
        initRecyclerView()
    }

    override
    fun setupObservers() {
        viewModel.products.observe(this) {
            hideLoading()
        }
    }

    private fun openIntro() {
        navigateSafe(LanguageFragmentDirections.actionLanguageFragmentToIntroPagerFragment())
    }

    private fun initRecyclerView() {
        val recyclerView = binding.purchaseRv

        adapter = PurchaseAdapter { item ->

        }

        val mLayoutManage = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = mLayoutManage
    }
}