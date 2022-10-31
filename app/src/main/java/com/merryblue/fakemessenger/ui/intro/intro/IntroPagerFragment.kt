package com.merryblue.fakemessenger.ui.intro.intro

import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.viewpager2.widget.ViewPager2
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.FragmentIntroPagerBinding
import com.merryblue.fakemessenger.enum.IntroPage
import com.merryblue.fakemessenger.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import org.app.common.BaseFragment
import org.app.common.extensions.openActivityAndClearStack

@AndroidEntryPoint
class IntroPagerFragment : BaseFragment<FragmentIntroPagerBinding>() {

    private val viewModel: IntroViewModel by viewModels()
    private val SLIDER_DELAY: Long = 2500

    private var pageChangedCallback = object : ViewPager2.OnPageChangeCallback() {
        override
        fun onPageSelected(position: Int) {
            viewModel.setCurrentPage(position)
        }
    }

    override
    fun getLayoutId() = R.layout.fragment_intro_pager

    override
    fun setBindingVariables() {
        binding.viewModel = viewModel
    }

    override fun setUpViews() {
        setupPager()
//        setUpAutoScrolling()
    }

    override fun setupObservers() {
        viewModel.openHomeEvent.observe(this) { openHome() }

        viewModel.currentPage.observe(this) {
            if (it == 0) {
                binding.introPager.setCurrentItem(it, false)
            } else {
                binding.introPager.setCurrentItem(it, true)
            }
        }
    }

    override fun onFragmentResume() {
        binding.introPager.registerOnPageChangeCallback(pageChangedCallback)
    }

    override fun onFragmentPause() {
        binding.introPager.unregisterOnPageChangeCallback(pageChangedCallback)
    }

    private fun setupPager() {
        val pagerAdapter = IntroPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        val viewPager = binding.introPager
        viewPager.adapter = pagerAdapter
    }

    private fun setUpAutoScrolling() {
        lifecycle.coroutineScope.launchWhenResumed {
            while (true) {
                delay(SLIDER_DELAY)
                var nextPos = 0
                if (viewModel.currentPage.value != (IntroPage.values().size - 1)) {
                    nextPos = viewModel.currentPage.value!! + 1
                }

                if (nextPos == 0) {
                    binding.introPager.setCurrentItem(nextPos, false)
                } else {
                    binding.introPager.setCurrentItem(nextPos, true)
                }
            }
        }
    }

    private fun openHome() {
        viewModel.setFirstTime(false)
        openActivityAndClearStack(HomeActivity::class.java)
    }
}