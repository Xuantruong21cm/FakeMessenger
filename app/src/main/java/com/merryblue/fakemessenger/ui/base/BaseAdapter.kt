package com.merryblue.fakemessenger.ui.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    open val ITEM_EMPTY = -1
    open val ITEM_NORMAL = 0

    fun compareDiffUtil(oldItem: T, newItem: T): Boolean = oldItem == newItem

    var items = mutableListOf<T>()
    var mDataFiltered = items

    override fun getItemId(position: Int): Long {
        val item = getItem(position)
        return item?.hashCode()?.toLong() ?: System.currentTimeMillis()
    }

    fun getItem(position: Int): T? {
        return if (position in 0 until itemCount) mDataFiltered[position] else null
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item == null) ITEM_EMPTY else ITEM_NORMAL
    }

    override fun getItemCount(): Int {
        return mDataFiltered.size
    }

    fun updateItem(index: Int, data: T?) {
        if (index != -1) {
            data?.let { mDataFiltered.set(index, it) }
            notifyItemChanged(index)
        }
    }

    fun addLastItem(data: T?) {
        data?.let {
            mDataFiltered.add(data)
            notifyItemChanged(mDataFiltered.size)
        }
    }

    fun submitData(list: ArrayList<T>?) {
        list?.let {
            if (mDataFiltered.isEmpty()) {
                mDataFiltered.addAll(it)
                notifyDataSetChanged()
            } else {
                notifyWithDiffUtil(mDataFiltered, list) {
                    mDataFiltered.clear()
                    mDataFiltered.addAll(list)
                }
            }
        }
    }

    fun removeItem(position: Int) {
        mDataFiltered.removeAt(position)
        notifyItemRangeChanged(position, itemCount - 1)
    }

    fun removeItem(data: T) {
        mDataFiltered.remove(data)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataFiltered.clear()
        notifyDataSetChanged()
    }

    private fun notifyWithDiffUtil(oldItems: List<T>, newItems: List<T>, updateData: () -> Unit) {
        val diff = DiffUtil.calculateDiff(
            object : DiffUtil.Callback() {

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return compareDiffUtil(
                        oldItems[oldItemPosition],
                        newItems[newItemPosition]
                    )
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return oldItems[oldItemPosition]?.equals(newItems[newItemPosition]) ?: false
                }

                override fun getOldListSize() = oldItems.size

                override fun getNewListSize() = newItems.size
            },
            false
        )
        updateData()
        diff.dispatchUpdatesTo(this)
    }
}
