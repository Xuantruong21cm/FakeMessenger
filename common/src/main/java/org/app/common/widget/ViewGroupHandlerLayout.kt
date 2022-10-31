package org.app.common.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.BindingAdapter
import org.app.common.R
import org.app.common.databinding.CommonViewEmptyBinding
import org.app.common.databinding.LayoutErrorInternetBinding
import org.app.common.databinding.LayoutNoInternetBinding
import org.app.common.extensions.isNetworkConnected
import org.app.common.extensions.setSafeOnClickListener

class ViewGroupHandlerLayout : RelativeLayout {
  private var layoutEmpty =
    LayoutInflater.from(context).inflate(R.layout.common_view_empty, null, false).apply {
      layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
      id = View.generateViewId()
    }
  private var layoutNoInternet =
    LayoutInflater.from(context).inflate(R.layout.layout_no_internet, null, false).apply {
      layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
      id = View.generateViewId()
    }

  private var layoutErrorInternet =
    LayoutInflater.from(context).inflate(R.layout.layout_error_internet, null, false).apply {
      layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
      id = View.generateViewId()
    }

  constructor(context: Context?) : this(context, null)
  constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
    context,
    attrs,
    defStyleAttr, 0
  )

  constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
  ) : super(context, attrs, defStyleAttr, defStyleRes) {
    this.id = R.id.viewGroupHandlerLayout
    initView(context, attrs, defStyleAttr, defStyleRes)
  }

  companion object {
    @JvmStatic
    @BindingAdapter("app:isEmpty", requireAll = false)
    fun setEmptyLayout(view: ViewGroupHandlerLayout, isEmpty: Boolean?) {
      if (isEmpty == true) view.showLayoutEmpty() else view.hideLayoutEmpty()
    }

    @JvmStatic
    @BindingAdapter("app:isErrorView", requireAll = false)
    fun setErrorLayout(view: ViewGroupHandlerLayout, isErrorView: Boolean?) {
      if (isErrorView == true) view.showLayoutErrorInternet() else view.hideLayoutErrorInternet()
    }
  }

  private fun initView(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
  ) {
    val objAttrs = context?.obtainStyledAttributes(attrs, R.styleable.ViewGroupHandlerLayout)
    var titleEmpty: String? = null
    var imgEmpty: Drawable? = null
    if (objAttrs?.hasValue(R.styleable.ViewGroupHandlerLayout_text_title_empty) == true) {
      titleEmpty = objAttrs.getString(R.styleable.ViewGroupHandlerLayout_text_title_empty)
    }
    if (objAttrs?.hasValue(R.styleable.ViewGroupHandlerLayout_text_img_empty) == true) {
      imgEmpty = objAttrs.getDrawable(R.styleable.ViewGroupHandlerLayout_text_img_empty)
    }
    objAttrs?.recycle()
  }

  private fun getListIdHandler(): List<Int> {
    return listOf(layoutNoInternet.id, layoutEmpty.id, layoutErrorInternet.id)
  }

  private fun hideChildrenLayout() {
    for (i in 0 until childCount) {
      val child = getChildAt(i)
      if (getListIdHandler().contains(child.id)) child.visibility = View.GONE
    }
  }

  fun initViewInflate() {
    hideLayoutEmpty()
    hideLayoutErrorInternet()
    if (context.isNetworkConnected()) hideLayoutNoInternet() else showLayoutNoInternet()
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    addView(layoutEmpty)
    addView(layoutNoInternet)
    addView(layoutErrorInternet)
    initViewInflate()
  }

  fun showLayoutEmpty() {
    hideChildrenLayout()
    this.layoutEmpty.visibility = View.VISIBLE
  }

  fun hideLayoutEmpty() {
    this.layoutEmpty.visibility = View.GONE
  }

  fun showLayoutNoInternet() {
    hideChildrenLayout()
    this.layoutNoInternet.visibility = View.VISIBLE
  }

  fun hideLayoutNoInternet() {
    this.layoutNoInternet.visibility = View.GONE
  }

  fun showLayoutErrorInternet() {
    hideChildrenLayout()
    layoutErrorInternet.visibility = View.VISIBLE
  }

  fun hideLayoutErrorInternet() {
    layoutErrorInternet.visibility = View.GONE
  }

  fun onRetryClick(block: () -> Unit) {
    val noInternet = LayoutNoInternetBinding.bind(layoutNoInternet)
    val errorInternet = LayoutErrorInternetBinding.bind(layoutErrorInternet)
    noInternet.btRetry.setSafeOnClickListener {
      if (context.isNetworkConnected()) {
        block()
        hideLayoutNoInternet()
      }
    }
    errorInternet.btRetry.setSafeOnClickListener {
      if (context.isNetworkConnected()) {
        block()
      }
    }
  }
}