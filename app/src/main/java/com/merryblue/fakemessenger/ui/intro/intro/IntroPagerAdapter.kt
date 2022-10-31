package com.merryblue.fakemessenger.ui.intro.intro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.merryblue.fakemessenger.enum.IntroPage

class IntroPagerAdapter(fm: FragmentManager,
                       lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return IntroPage.values().size
    }

    override fun createFragment(position: Int): Fragment {
        return IntroFragment.newInstance(position)
    }
}