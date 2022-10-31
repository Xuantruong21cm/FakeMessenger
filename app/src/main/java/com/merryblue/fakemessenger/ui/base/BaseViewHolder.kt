package com.merryblue.fakemessenger.ui.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    open fun onBind(position: Int, model: Any?) {}
}

open class BaseBindingViewHolder<ViewBinding : ViewDataBinding>(open val binding: ViewBinding) :
    BaseViewHolder(binding.root)
