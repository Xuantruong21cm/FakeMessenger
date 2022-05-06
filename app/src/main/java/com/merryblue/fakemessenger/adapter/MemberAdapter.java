package com.merryblue.fakemessenger.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.merryblue.fakemessenger.R;
import com.merryblue.fakemessenger.databinding.ItemMemberBinding;
import com.merryblue.fakemessenger.model.UserMessageModel;
import com.merryblue.fakemessenger.ImageUtils;

import java.util.List;

public class MemberAdapter extends BaseRecyclerAdapter<UserMessageModel, MemberAdapter.ViewHolder> {

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClickListener(int position, View view);

        void onAvatarClickListener(int position);

    }

    public MemberAdapter(Context context, List<UserMessageModel> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.ViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMemberBinding memberBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memberBinding = ItemMemberBinding.bind(itemView);
        }

        public void bindData(UserMessageModel userMessageModel) {
            if (userMessageModel == null)
                return;
            ImageUtils.loadImage(memberBinding.imAvatar, userMessageModel.getAvatar());
            if (!TextUtils.isEmpty(userMessageModel.getName()))
                memberBinding.tvName.setText(userMessageModel.getName());
            memberBinding.imMore.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onItemClickListener(getAdapterPosition(), memberBinding.imMore);
            });

            memberBinding.imAvatar.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onAvatarClickListener(getAdapterPosition());
            });
        }
    }
}
