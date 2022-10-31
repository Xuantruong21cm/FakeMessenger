package com.merryblue.fakemessenger.ui.base

import androidx.databinding.ViewDataBinding

abstract class BaseBindingAdapter<T> : BaseAdapter<T, BaseBindingViewHolder<ViewDataBinding>>() {
    abstract fun getLayoutRes(viewType: Int): Int
}
