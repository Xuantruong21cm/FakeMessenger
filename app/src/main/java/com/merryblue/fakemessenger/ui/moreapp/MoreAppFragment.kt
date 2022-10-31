package com.merryblue.fakemessenger.ui.moreapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.viewModels
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.data.model.OtherAppModel
import com.merryblue.fakemessenger.databinding.FragmentMoreAppLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import org.app.common.BaseFragment

@AndroidEntryPoint
class MoreAppFragment : BaseFragment<FragmentMoreAppLayoutBinding>(),
MoreAppAdapter.ItemOtherAppListener {

    private val viewModel: MoreAppViewModel by viewModels()
    private val adapter by lazy { MoreAppAdapter(this) }
    override
    fun getLayoutId() = R.layout.fragment_more_app_layout

    override
    fun setBindingVariables() {
        binding.viewModel = viewModel
    }

    override fun initView(view: View) {
        showLoading("Loading...")
    }

    override
    fun setUpViews() {
        viewModel.getOtherApps()
        binding.recyclerViewOtherApp.adapter = adapter
    }

    override fun setupObservers() {
        viewModel.appListEvent.observe(this) {
            hideLoading()
            adapter.submitData(ArrayList(it))
        }
    }

    override fun onOpenStore(item: OtherAppModel) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${item.id}")))
        } catch (ex: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${item.id}")
                )
            )
        }
    }
}