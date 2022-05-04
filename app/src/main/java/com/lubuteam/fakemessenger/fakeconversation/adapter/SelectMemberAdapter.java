package com.lubuteam.fakemessenger.fakeconversation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.fakemessenger.fakeconversation.R;
import com.lubuteam.fakemessenger.fakeconversation.databinding.ItemSelectMemberBinding;
import com.lubuteam.fakemessenger.fakeconversation.model.UserMessageModel;
import com.lubuteam.fakemessenger.fakeconversation.utils.ImageUtils;

import java.util.List;

public class SelectMemberAdapter extends BaseRecyclerAdapter<UserMessageModel, SelectMemberAdapter.ViewHolder> {
    public SelectMemberAdapter(Context context, List<UserMessageModel> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(SelectMemberAdapter.ViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public SelectMemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_select_member, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemSelectMemberBinding selectMemberBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selectMemberBinding = ItemSelectMemberBinding.bind(itemView);
        }

        public void bindData(UserMessageModel userMessageModel) {
            if (userMessageModel == null)
                return;
            ImageUtils.loadImage(selectMemberBinding.imAvatar, userMessageModel.getAvatar());
            if (!TextUtils.isEmpty(userMessageModel.getName()))
                selectMemberBinding.tvName.setText(userMessageModel.getName());

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(getAdapterPosition());
            });
        }
    }
}
