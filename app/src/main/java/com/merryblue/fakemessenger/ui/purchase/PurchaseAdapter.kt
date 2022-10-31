package com.merryblue.fakemessenger.ui.purchase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.merryblue.fakemessenger.databinding.ItemPurchaseProductBinding
import com.merryblue.fakemessenger.ui.base.BaseAdapter
import com.merryblue.fakemessenger.ui.base.BaseBindingViewHolder
import org.app.data.model.InAppProductModel
import com.merryblue.fakemessenger.BR

class PurchaseAdapter(val itemClick: (InAppProductModel) -> Unit)
    : BaseAdapter<InAppProductModel, BaseBindingViewHolder<ViewDataBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<ViewDataBinding> {
        val binding = ItemPurchaseProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ViewDataBinding>, position: Int) {
        val model = getItem(position)
        when (holder.binding) {
            is ItemPurchaseProductBinding -> {
                holder.binding.setVariable(BR.data, model)
            }
        }
    }
}