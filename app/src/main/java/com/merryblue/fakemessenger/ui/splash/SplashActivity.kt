package com.merryblue.fakemessenger.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.ActivitySplashBinding
import com.merryblue.fakemessenger.ui.home.HomeActivity
import com.merryblue.fakemessenger.ui.intro.IntroActivity
import org.app.common.BaseActivity
import org.app.common.extensions.openActivityAndClearStack

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

  private val viewModel: SplashViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.activity_splash

  override
  fun setUpViews() {
    decideNavigationLogic()
  }

  private fun decideNavigationLogic() {
    Handler(Looper.getMainLooper()).postDelayed({
      val targetActivity = if (viewModel.isFirstTime()) {
        IntroActivity::class.java
      } else {
        HomeActivity::class.java
      }
      openActivityAndClearStack(targetActivity)
    }, 300)
  }
}