package com.merryblue.fakemessenger.ui.intro.language

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.FragmentLanguageBinding
import org.app.common.BaseFragment
import org.app.common.extensions.backToPreviousScreen
import org.app.common.extensions.navigateSafe
import timber.log.Timber

@AndroidEntryPoint
class LanguageFragment : BaseFragment<FragmentLanguageBinding>() {

  private val viewModel: LanguageViewModel by viewModels()
  lateinit var adapter: LanguageAdapter

  private val safeArgs: LanguageFragmentArgs by navArgs()

  override
  fun getLayoutId() = R.layout.fragment_language

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
    viewModel.openIntro.observe(this) { openIntro() }
  }

  private fun openIntro() {
    navigateSafe(LanguageFragmentDirections.actionLanguageFragmentToIntroPagerFragment())
  }

  private fun initRecyclerView() {
    val recyclerView = binding.languageRv

    context?.let {
      val languages = viewModel.getLanguage()
      adapter = LanguageAdapter(languages,
        itemClick = { item ->
          val source = safeArgs.source
          Log.i(TAG, "Language Fragment from source: $source")
          viewModel.updateUserLanguage(item)
          if (source == "intro") {
            openIntro()
          } else {
            backToPreviousScreen()
          }
        })

      val mLayoutManage = LinearLayoutManager(context)
      recyclerView.adapter = adapter
      recyclerView.layoutManager = mLayoutManage
    } ?: kotlin.run {
      Timber.i(TAG, "Null context -> Should not go here!!!")
    }
  }
}