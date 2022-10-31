package com.merryblue.fakemessenger.ui.moreapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.data.model.OtherAppModel
import com.merryblue.fakemessenger.databinding.ItemOtherAppBinding
import com.merryblue.fakemessenger.ui.base.BaseBindingAdapter
import com.merryblue.fakemessenger.ui.base.BaseBindingViewHolder
import com.merryblue.fakemessenger.BR

class MoreAppAdapter(val listener: ItemOtherAppListener) : BaseBindingAdapter<OtherAppModel>() {

    override fun getLayoutRes(viewType: Int): Int = R.layout.item_other_app

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<ViewDataBinding> {
        val binding = ItemOtherAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ViewDataBinding>, position: Int) {
        val model = getItem(position)
        when (holder.binding) {
            is ItemOtherAppBinding -> {
                holder.binding.setVariable(BR.placeHolder, R.drawable.ic_app_logo)
                holder.binding.setVariable(BR.errorHolder, R.drawable.ic_app_logo)
                holder.binding.setVariable(BR.otherApp, model)
                holder.binding.setVariable(BR.listener, listener)
            }
        }
    }

    interface ItemOtherAppListener {
        fun onOpenStore(item: OtherAppModel)
    }
}
