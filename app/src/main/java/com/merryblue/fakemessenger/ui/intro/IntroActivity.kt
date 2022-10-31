package com.merryblue.fakemessenger.ui.intro

import dagger.hilt.android.AndroidEntryPoint
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.ActivityIntroBinding
import org.app.common.BaseActivity

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

  override
  fun getLayoutId() = R.layout.activity_intro
}