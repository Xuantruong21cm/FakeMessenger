package com.merryblue.fakemessenger.ui.intro.intro

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint
import org.app.common.BaseFragment


@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding>() {

  private val viewModel: IntroViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_intro

  override fun getFragmentArguments() {
    arguments?.let {
      val arg = it.getInt(ARG_PAGE_NUMBER, 0)
      viewModel.setPageIndex(arg)
    }
  }

  override
  fun setUpViews() {

  }

  override
  fun setupObservers() {
    viewModel.pageModel.observe(this) {
      binding.data = it
    }
  }

  companion object {
    private const val ARG_PAGE_NUMBER = "page_number"

    @JvmStatic
    fun newInstance(pageNumber: Int): IntroFragment {
      return IntroFragment().apply {
        arguments = Bundle().apply {
          putInt(ARG_PAGE_NUMBER, pageNumber)
        }
      }
    }
  }
}