package com.merryblue.fakemessenger.ui.adapter.homeAdapter

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.data.model.Contact
import com.merryblue.fakemessenger.databinding.ItemCategoriesBinding
import com.merryblue.fakemessenger.databinding.ItemFakeFuntionBinding
import com.merryblue.fakemessenger.databinding.ItemSpaceBottomBinding
import com.merryblue.fakemessenger.ui.adapter.ConversationCallback
import com.merryblue.fakemessenger.ui.adapter.ConversationType
import com.merryblue.fakemessenger.ui.adapter.ConversationsAdapter
import org.app.common.binding.setOnSingleClickListener
import org.app.common.extensions.dipToPix

class HomeAdapter(
    val context: Activity,
    private val itemWidth: Int = 0
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var bottomHeight = 0
    var lastConversationsAdapter =
        ConversationsAdapter(context, itemWidth, ConversationType.TYPE_LAST_CONVERSATION)
    private var homeListener : HomeAdapterCallback? = null

    fun setDataLastConversation(list: ArrayList<Contact>) {
        lastConversationsAdapter.setData(list)
    }

    fun setListener(listener : HomeAdapterCallback){
        this.homeListener = listener
    }

    fun updateBottomSpace(itemHeight: Int) {
        this.bottomHeight = itemHeight
        notifyItemChanged(4)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_FAKE_FUNTION -> {
                FakeFuntionViewHolder(ItemFakeFuntionBinding.inflate(inflater, parent, false))
            }
            VIEW_TYPE_ITEM_CATEGORIES -> {
                ItemHomeCategoriesViewHolder(ItemCategoriesBinding.inflate(inflater, parent, false))
            }
            else -> {
                SpaceBottomHomeViewHolder(ItemSpaceBottomBinding.inflate(inflater, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FakeFuntionViewHolder) {
            val padding = (itemWidth * 0.0444444).toInt()

            holder.mBiding.imgFakeVideoCall.setOnSingleClickListener {
                homeListener?.onFakeVideoCall()
            }

            holder.mBiding.imgFakeMessage.setOnSingleClickListener {
                homeListener?.onFakeMessage()
            }

            holder.mBiding.imgFakeNotification.setOnSingleClickListener {
                homeListener?.onFakeNotification()
            }

            holder.mBiding.imgFakeVoiceCall.setOnSingleClickListener {
                homeListener?.onFakeVoiceCall()
            }
        } else if (holder is ItemHomeCategoriesViewHolder) {

            val padding = (itemWidth * 0.0444444).toInt()
            holder.mBinding.tvTitleCategories.setPadding(padding,(padding*1.2).toInt(),0,padding)
            holder.mBinding.tvTitleCategories.text = if (position == 1) {
                context.getString(R.string.txt_last_conversation)
            } else {
                context.getString(R.string.txt_more_apps)
            }

            while (holder.mBinding.rvContent.itemDecorationCount > 0) {
                holder.mBinding.rvContent.removeItemDecorationAt(0)
            }

            holder.mBinding.rvContent.minimumHeight = dipToPix(100f,context).toInt()

            when (position) {
                1 -> {
                    holder.mBinding.rvContent.layoutManager = LinearLayoutManager(context)
                    holder.mBinding.rvContent.adapter = lastConversationsAdapter
                    val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                    val insetDrawable = InsetDrawable(ColorDrawable(ContextCompat.getColor(context,R.color.color1F2937)),0,0,0,0)
                    divider.setDrawable(insetDrawable)
                    holder.mBinding.rvContent.addItemDecoration(divider)
                    holder.mBinding.rvContent.setPadding(padding,0,padding,0)
                    lastConversationsAdapter.setListener(object : ConversationCallback {
                        override fun lastConversationClick(contact: Contact) {
                            super.lastConversationClick(contact)
                            homeListener?.onConversationclick(contact)
                        }
                    })
                }
                2 -> {
                    holder.mBinding.rvContent.minimumHeight = dipToPix(300f,context).toInt()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                VIEW_TYPE_FAKE_FUNTION
            }
            3 -> {
                VIEW_TYPE_BOTTOM_SPACE
            }
            else -> {
                VIEW_TYPE_ITEM_CATEGORIES
            }
        }
    }

    override fun getItemCount(): Int {
        return FIXED_ITEM_HOME
    }

    companion object {
        const val FIXED_ITEM_HOME = 4
        const val VIEW_TYPE_FAKE_FUNTION = 0
        const val VIEW_TYPE_ITEM_CATEGORIES = 1
        const val VIEW_TYPE_BOTTOM_SPACE = 2
    }

    inner class FakeFuntionViewHolder(val mBiding: ItemFakeFuntionBinding) :
        RecyclerView.ViewHolder(mBiding.root)

    inner class ItemHomeCategoriesViewHolder(val mBinding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    inner class SpaceBottomHomeViewHolder(val mBinding: ItemSpaceBottomBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        init {
            val param = mBinding.root.layoutParams
            param?.height = bottomHeight
            mBinding.root.layoutParams = param
        }
    }
}

interface HomeAdapterCallback {
    fun onFakeMessage(){}
    fun onFakeVideoCall(){}
    fun onFakeVoiceCall(){}
    fun onFakeNotification(){}
    fun onConversationclick(contact: Contact){}

}