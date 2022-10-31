package org.app.common.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<VB : ViewBinding, D>(private val vb: VB) :
    RecyclerView.ViewHolder(vb.root) {

    var itemClickListener: ((D, Int) -> Unit)? = null

    fun bindView(data: D, position: Int) {
        vb.bindView(data, position)
    }

    protected abstract fun VB.bindView(data: D, position: Int)
}