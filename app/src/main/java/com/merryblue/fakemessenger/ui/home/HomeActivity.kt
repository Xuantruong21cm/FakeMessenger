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

  private lateinit var appBarConfiguration: AppBarConfiguration

  override
  fun getLayoutId() = R.layout.activity_home

  override fun setUpViews() {
    setSupportActionBar(binding.appBarMain.toolbar)

    val drawerLayout: DrawerLayout = binding.drawerLayout
    val navView: NavigationView = binding.navView

    val navController = findNavController(R.id.nav_host_fragment_content_main)
    appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.navDrawerHome,
        R.id.language,
        R.id.moreApp,
        R.id.feedback,
        R.id.privacy,
      ), drawerLayout
    )
    setupActionBarWithNavController(navController, appBarConfiguration)
    navView.setupWithNavController(navController)

    binding.navView.getHeaderView(0).apply {
      this.setOnClickListener {
        Log.i(TAG, "Go to Premium Subscribe")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as? NavHostFragment
        if (navHostFragment?.navController != null) {
          val navController = navHostFragment.navController
          binding.drawerLayout.close()
        }
      }
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_main)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

}