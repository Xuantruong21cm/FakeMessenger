package org.app.common

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl
import kotlinx.coroutines.launch
import org.app.common.utils.NetworkUtil
import org.app.common.utils.getDialogWaiting
import org.app.common.utils.hideKeyword
import org.app.common.widget.CustomToast
import java.util.*

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {
  open val TAG = this::class.simpleName

  private val localeDelegate: LocaleHelperActivityDelegate = LocaleHelperActivityDelegateImpl()

  private var _binding: VB? = null
  open val binding get() = _binding!!

  private val progressDialog: Dialog by lazy { getDialogWaiting(this) }

  private var networkStateChangeReceiver: BroadcastReceiver? = null

  private var isInternetConnected = true

  private var toastNoInternet: Toast? = null

  private var toastHasInternet: Toast? = null

  var isFirstTimeLaunch = true

  open fun preventShowToastNoInternet() = false

  open fun toastPaddingBottom(): Int? = null

  override
  fun createConfigurationContext(overrideConfiguration: Configuration): Context {
    val context = super.createConfigurationContext(overrideConfiguration)
    return LocaleHelper.onAttach(context)
  }

  override
  fun getApplicationContext(): Context =
    localeDelegate.getApplicationContext(super.getApplicationContext())

  override
  fun onResume() {
    super.onResume()
    localeDelegate.onResumed(this)
  }

  override
  fun onPause() {
    super.onPause()
    localeDelegate.onPaused()
  }

  override
  fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Runs the block of code in a coroutine when the lifecycle is at least STARTED.
    // The coroutine will be cancelled when the ON_STOP event happens and will
    // restart executing if the lifecycle receives the ON_START event again.
    lifecycleScope.launch {
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        onActivityStarted()
      }
    }

    initViewBinding()
    setContentView(binding.root)

    if (savedInstanceState == null) {
      setUpBottomNavigation()
    }

    toastNoInternet = CustomToast.makeText(
      this,
      getString(R.string.no_internet_connection_title),
      R.drawable.ic_common_error
    )
    toastHasInternet = CustomToast.makeText(
      this,
      getString(R.string.has_internet_connection_title),
      R.drawable.ic_tick_correct,
      toastPaddingBottom()
    )
    networkStateChangeReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
          context?.let {
            if (isFirstTimeLaunch) {
              isFirstTimeLaunch = false
              return
            }
            isInternetConnected = NetworkUtil.isNetworkConnected(it)
            onNetworkStateChanged(isInternetConnected)
            if (!isInternetConnected && !preventShowToastNoInternet() && lifecycle.currentState.isAtLeast(
                Lifecycle.State.RESUMED
              )
            ) {
              toastNoInternet?.show()
            } else {
              toastNoInternet?.cancel()
            }
          }
        }
      }
    }

    applicationContext.registerReceiver(
      networkStateChangeReceiver,
      IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    )

    setUpViews()
  }

  override
  fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)

    setUpBottomNavigation()
  }

  protected open fun getOption(tag: String?): FragmentControllerOption {
    return FragmentControllerOption.Builder()
      .setTag(tag)
      .useAnimation(false)
      .addBackStack(false)
      .isTransactionReplace(true)
      .option
  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount > 0) {
      supportFragmentManager.popBackStack()
    } else {
      super.onBackPressed()
    }
  }

  private fun initViewBinding() {
    _binding = DataBindingUtil.setContentView(this, getLayoutId())
    binding.lifecycleOwner = this
    binding.executePendingBindings()
    hideKeyword(binding.root, this)
  }

  @LayoutRes
  abstract fun getLayoutId(): Int

  open fun setUpBottomNavigation() {}

  open fun setUpViews() {}

  // Override this function for task that need run background and may need time to completed
  open fun onActivityStarted() {}

  open fun updateLocale(language: String) {
    localeDelegate.setLocale(this, Locale(language))
  }

  override
  fun getDelegate() = localeDelegate.getAppCompatDelegate(super.getDelegate())

  protected open fun onNetworkStateChanged(isConnected: Boolean) {}

  protected open fun getChildLayoutReplace(): Int {
    return 0
  }

  open fun onLeft(view: View) {
    if (supportFragmentManager.backStackEntryCount > 0) {
      supportFragmentManager.popBackStack()
    } else {
      this.onBackPressed()
    }
  }

  override fun onDestroy() {
    applicationContext.unregisterReceiver(networkStateChangeReceiver)
    networkStateChangeReceiver = null
    _binding = null
    super.onDestroy()
  }

  fun showProgressDialog() {
    binding.root.post {
      if (!progressDialog.isShowing) progressDialog.show()
    }
  }

  fun hideProgressDialog() {
    binding.root.post {
      if (progressDialog.isShowing) progressDialog.dismiss()
    }
  }

  fun showToastHasInternet() {
    // show if activity resume
    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
      toastHasInternet?.show()
    }
  }

  fun hideToastHasInternet() {
    toastHasInternet?.cancel()
  }

  fun showToastNoInternet() {
    toastNoInternet?.show()
  }

  fun replaceFragment(fragment: Fragment, resId : Int ) {
    supportFragmentManager.beginTransaction().setCustomAnimations(
      R.anim.enter_from_right,
      R.anim.exit_to_left,
      R.anim.enter_from_left,
      R.anim.exit_to_right)
      .replace(resId, fragment)
      .addToBackStack(fragment::class.simpleName)
      .commit()
  }
}