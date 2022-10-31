package org.app.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.app.common.utils.hideKeyword
import org.app.common.utils.hideLoadingDialog
import org.app.common.utils.showLoadingDialog
import java.util.*

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {
  open val TAG = this::class.simpleName

  private var _binding: VB? = null
  open val binding get() = _binding!!
  private var mRootView: View? = null
  private var hasInitializedRootView = false
  private var progressDialog: Dialog? = null

  override
  fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    if (mRootView == null) {
      initViewBinding(inflater, container)
    }

    hideKeyword(binding.root, activity)
    initView(binding.root)
    // using when fragment transition animation 300ms
    binding.root.postDelayed(
      { initDataWithAnimation() },
      300
    )
    initObserver()

    return mRootView
  }

  private fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
    _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

    mRootView = binding.root
    binding.lifecycleOwner = this
    binding.executePendingBindings()
  }

  override
  fun onResume() {
    super.onResume()

    registerListeners()
  }

  override
  fun onPause() {
    unRegisterListeners()

    super.onPause()
  }

  override
  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Use LifecycleObserver instead of override lifecycle methods such as onResume
    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
      override fun onResume(owner: LifecycleOwner) {
        onFragmentResume()
      }
      override fun onPause(owner: LifecycleOwner) {
        onFragmentPause()
      }
    })

    if (!hasInitializedRootView) {
      getFragmentArguments()
      setBindingVariables()
      observeAPICall()
      setupObservers()
      setUpViews()

      hasInitializedRootView = true
    }
    view.setOnTouchListener { _, _ -> true }
  }

  override fun onDestroyView() {
    if (view?.parent != null) {
      (view?.parent as? ViewGroup)?.endViewTransition(view)
    }
    hideLoading()
    super.onDestroyView()
  }

  protected open fun getOption(tag: String?): FragmentControllerOption {
    return FragmentControllerOption.Builder()
      .setTag(tag)
      .useAnimation(true)
      .addBackStack(true)
      .isTransactionReplace(true)
      .option
  }

  protected open fun getOption(tag: String?, isReplaceFrag: Boolean): FragmentControllerOption {
    return FragmentControllerOption.Builder()
      .setTag(tag)
      .useAnimation(true)
      .addBackStack(true)
      .isTransactionReplace(isReplaceFrag)
      .option
  }

  @LayoutRes
  abstract fun getLayoutId(): Int

  open fun initView(view: View) {}

  open fun initDataWithAnimation() {}

  open fun registerListeners() {}

  open fun unRegisterListeners() {}

  open fun getFragmentArguments() {}

  open fun setBindingVariables() {}

  open fun setUpViews() {}

  open fun onFragmentResume() {}

  open fun onFragmentPause() {}

  open fun observeAPICall() {}

  open fun setupObservers() {}

  protected open fun onRetryClick() {}

  protected open fun reloadData() {}

  open fun onLeft(view: View) {
    if (parentFragmentManager.backStackEntryCount > 0) {
      parentFragmentManager.popBackStack()
    } else {
      activity?.onBackPressed()
    }
  }

  open fun onRight(view: View) {
  }

  open fun onRight1(view: View) {
  }

  protected open fun getChildLayoutReplace(): Int {
    return 0
  }

  fun showLoading() {
    hideLoading()
    progressDialog = showLoadingDialog(requireActivity(), null)
  }

  fun showLoading(hint: String?) {
    hideLoading()
    progressDialog = showLoadingDialog(requireActivity(), hint)
  }

  fun hideLoading() = hideLoadingDialog(progressDialog, requireActivity())

  fun setLanguage(language: String) {
    (requireActivity() as BaseActivity<*>).updateLocale(language)
  }

  open fun initObserver(){}

  open fun showMessage(message : String){
    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
  }

  val currentLanguage: Locale
    get() = Locale.getDefault()
}