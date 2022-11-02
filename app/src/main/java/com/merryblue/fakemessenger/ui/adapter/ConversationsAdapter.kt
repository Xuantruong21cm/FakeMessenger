package com.merryblue.fakemessenger.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.merryblue.fakemessenger.data.model.Contact
import com.merryblue.fakemessenger.databinding.ItemContactConversationBinding
import com.merryblue.fakemessenger.databinding.ItemStartCallConversationBinding
import com.merryblue.fakemessenger.databinding.ItemStartConversationBinding
import org.app.common.binding.setImage
import org.app.common.binding.setOnSingleClickListener
import org.app.common.extensions.ImageViewType
import org.app.common.extensions.loadImageUrl
import org.app.common.extensions.show
import org.app.common.utils.showMessage
import timber.log.Timber

class ConversationsAdapter(
    val context: Context,
    val itemWidth: Int = 0,
    val type: ConversationType
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mConversationList = mutableListOf<Contact>()
    private var mItemTag = 1
    private var conversationCallback : ConversationCallback? = null

    fun setData(list: ArrayList<Contact>) {
        this.mConversationList.clear()
        this.mConversationList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            START_CALL_CONVERSATION -> StartCallConversationViewHolder(
                ItemStartCallConversationBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
            START_CONVERSATION ->{
                mItemTag++
                StartConversationViewHolder(
                    ItemStartConversationBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                        ,mItemTag)
            }
            LAST_CONVERSATION-> ContactConversationViewHolder(
                ItemContactConversationBinding.inflate(LayoutInflater.from(context), parent, false)
            )
            else -> StartCallConversationViewHolder(
                ItemStartCallConversationBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StartCallConversationViewHolder -> {
                holder.mBinding.root.setOnSingleClickListener {
                    conversationCallback?.startCall()
                }
            }
            is StartConversationViewHolder -> {
               holder.mBinding.root.setOnSingleClickListener {
                   showMessage(context,"test start conversation")
               }
                val conversation = mConversationList[position-1]
                holder.mBinding.imgAvatar.loadImageUrl(conversation.picturePath, ImageViewType.CIRCLE)
                holder.mBinding.tvConversationName.text = conversation.name
                holder.mBinding.viewOnlineStatus.show()
            }
            is ContactConversationViewHolder -> {
                when (type) {
                    ConversationType.TYPE_LAST_CONVERSATION -> {
                        val conversation = mConversationList[position]
                        holder.mBinding.imgAvatar.loadImageUrl(conversation.picturePath,ImageViewType.CIRCLE)
                        holder.mBinding.imgAvatarSeen.loadImageUrl(conversation.picturePath,ImageViewType.CIRCLE)
                        holder.mBinding.viewOnlineStatus.show()
                        holder.mBinding.tvUsernameLastConversation.text = conversation.name
                        holder.mBinding.tvTimeLastConversation.text = conversation.time
                        holder.mBinding.tvLastMessage.text = conversation.lastMessage

                        holder.mBinding.clInfoConversation.setPadding((itemWidth * 0.0333).toInt(),0,if (holder.mBinding.imgAvatarSeen.visibility == View.VISIBLE){
                            (itemWidth * 0.0333).toInt()
                        }else{0},0)

                        holder.mBinding.root.setOnSingleClickListener {
                            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION){
                                conversationCallback?.lastConversationClick(mConversationList[holder.bindingAdapterPosition])
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (type.ordinal) {
            ConversationType.TYPE_START_CONVERSATION.ordinal -> {
                return if (position == 0) {
                    START_CALL_CONVERSATION
                } else {
                    START_CONVERSATION
                }
            }

            ConversationType.TYPE_LAST_CONVERSATION.ordinal -> {
                return LAST_CONVERSATION
            }

            else -> {
                0
            }
        }
    }

    override fun getItemCount(): Int {
        return when (type){
            ConversationType.TYPE_START_CONVERSATION ->{
                mConversationList.size + 1
            }

            ConversationType.TYPE_LAST_CONVERSATION ->{
                5
            }

            else -> mConversationList.size
        }
    }

    companion object {
        const val START_CALL_CONVERSATION = 0
        const val START_CONVERSATION = 1
        const val LAST_CONVERSATION = 2
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is StartConversationViewHolder){
            Timber.d("on item recycle : ${holder.tag}")
        }
        super.onViewRecycled(holder)
    }

    fun setListener(listener : ConversationCallback){
        this.conversationCallback = listener
    }

    inner class StartConversationViewHolder(val mBinding : ItemStartConversationBinding,val tag :Int) : RecyclerView.ViewHolder(mBinding.root) {
        init {
            if (itemWidth != 0){
                val params = mBinding.root.layoutParams
                params.width = (itemWidth * 0.2).toInt()
                mBinding.root.layoutParams = params
                mBinding.tvConversationName.setPadding((params.width*0.05555).toInt(),0,(params.width*0.05555).toInt(),0)
            }
            Timber.d("on item created : $tag")
        }
    }

    inner class StartCallConversationViewHolder(val mBinding : ItemStartCallConversationBinding): RecyclerView.ViewHolder(mBinding.root){
        init {
            if (itemWidth != 0){
                val params = mBinding.root.layoutParams
                params.width = (itemWidth * 0.23333).toInt()
                mBinding.root.layoutParams = params
            }
        }
    }

    inner class ContactConversationViewHolder(val mBinding : ItemContactConversationBinding) : RecyclerView.ViewHolder(mBinding.root)

}

enum class ConversationType {
    TYPE_START_CONVERSATION, TYPE_LAST_CONVERSATION
}

interface ConversationCallback {
    fun startCall(){}
    fun lastConversationClick(contact: Contact){}
}