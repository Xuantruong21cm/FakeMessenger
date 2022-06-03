package com.vuthaihung.fancydialouge.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuthaihung.fancydialouge.R;
import com.vuthaihung.fancydialouge.databinding.ItemConversationVerticalBinding;
import com.vuthaihung.fancydialouge.model.ConversationModel;
import com.vuthaihung.fancydialouge.model.UserMessageModel;
import com.vuthaihung.fancydialouge.Config;
import com.vuthaihung.fancydialouge.ImageUtils;
import com.vuthaihung.fancydialouge.PreferencesHelper;
import com.vuthaihung.fancydialouge.Toolbox;

import java.util.List;

public class ConversationVerticalAdapter extends BaseRecyclerAdapter<ConversationModel
        , ConversationVerticalAdapter.ViewHolder> {

    private ItemLongClick itemLongClick;

    public interface ItemLongClick {
        void onItemLongClickListener(int index, View itemView);
    }

    public ConversationVerticalAdapter(Context context, List<ConversationModel> list, ItemLongClick itemLongClick) {
        super(context, list);
        this.itemLongClick = itemLongClick;
    }

    @Override
    public void onBindViewHolder(ConversationVerticalAdapter.ViewHolder holder, int position) {
        holder.binData(list.get(position));
    }

    @Override
    public ConversationVerticalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_conversation_vertical, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemConversationVerticalBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemConversationVerticalBinding.bind(itemView);
        }

        public void binData(ConversationModel model) {
            if (model == null)
                return;
            binding.imStatus.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(model.getName()))
                binding.tvName.setText(model.getName());
            ImageUtils.loadImage(binding.imOutline, model.getImage());

            binding.tvDatetime.setText(Toolbox.getDistanceTime(model.getLastTimeChat()));
            binding.tvContent.setText(Config.getLastMessage(mContext, model));

            if (model.isGroup()) {
                binding.imOutline.setVisibility(View.GONE);
                binding.llAvatarGroup.setVisibility(View.VISIBLE);
                List<UserMessageModel> lstMember = model.getUserMessageModels();
                if (!TextUtils.isEmpty(model.getImage()) && !"null".equals(model.getImage())) {
                    ImageUtils.loadImage(binding.imAvaGroup1, model.getImage());
                    if (lstMember.size() > 0) {
                        ImageUtils.loadImage(binding.imAvaGroup2, lstMember.get(0).getAvatar());
                    }
                } else {
                    if (lstMember.size() > 0) {
                        ImageUtils.loadImage(binding.imAvaGroup1, lstMember.get(0).getAvatar());
                    }
                    if (lstMember.size() > 1) {
                        ImageUtils.loadImage(binding.imAvaGroup2, lstMember.get(1).getAvatar());
                    }
                }
            } else {
                binding.imOutline.setVisibility(View.VISIBLE);
                binding.llAvatarGroup.setVisibility(View.GONE);
            }

            binding.tvContent.setTypeface(null, Typeface.NORMAL);
            binding.tvName.setTypeface(null, Typeface.NORMAL);
            if (model.getStatus() == Config.STATUS_SEEN) {
                ImageUtils.loadImage(binding.imStatus, model.isGroup()
                        ? PreferencesHelper.getString(PreferencesHelper.PHOTO_AVATAR_PATH)
                        : model.getImage());
            } else if (model.getStatus() == Config.STATUS_RECEIVED) {
//                binding.imStatus.setImageResource(R.drawable.bg_round_0084f0);
                binding.tvContent.setTypeface(null, Typeface.BOLD);
                binding.tvName.setTypeface(null, Typeface.BOLD);
            } else if (model.getStatus() == Config.STATUS_NOT_SEND) {
                binding.imStatus.setImageResource(R.drawable.ic_check_2);
            } else if (model.getStatus() == Config.STATUS_NOT_RECEIVED) {
                binding.imStatus.setImageResource(R.drawable.ic_check);
            }

            if (model.isActive()) {
                binding.imActive.setVisibility(View.VISIBLE);
            } else binding.imActive.setVisibility(View.GONE);


            if (model.getMessageModels().size() <= 1) {
                binding.imStatus.setVisibility(View.INVISIBLE);
            } else if (model.getMessageModels().get(model.getMessageModels().size() - 1).isSend()) {
                binding.imStatus.setVisibility(View.VISIBLE);
            } else {
                binding.imStatus.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(getAdapterPosition());
            });

            itemView.setOnLongClickListener(v -> {
                if (itemLongClick != null)
                    itemLongClick.onItemLongClickListener(getAdapterPosition(), itemView);
                return true;
            });
        }
    }
}
