package com.merryblue.fakemessenger.ui.home

import android.util.Log
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import org.app.common.BaseActivity

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

  override fun setUpViews() {
      replaceFragment(HomeFragment(),R.id.layout_Home)
  }

  override
  fun getLayoutId() = R.layout.activity_home

}