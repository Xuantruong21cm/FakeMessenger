package com.merryblue.fakemessenger.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.merryblue.fakemessenger.R;
import com.merryblue.fakemessenger.databinding.ItemConversationHorizontalBinding;
import com.merryblue.fakemessenger.model.ConversationModel;
import com.merryblue.fakemessenger.ImageUtils;

import java.util.List;

public class ConversationHorizontalAdapter extends BaseRecyclerAdapter<ConversationModel
        , ConversationHorizontalAdapter.ViewHolder> {

    public ConversationHorizontalAdapter(Context context, List<ConversationModel> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ConversationHorizontalAdapter.ViewHolder holder, int position) {
        holder.binData(list.get(position));
    }

    @Override
    public ConversationHorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_conversation_horizontal, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemConversationHorizontalBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemConversationHorizontalBinding.bind(itemView);
        }

        public void binData(ConversationModel model) {
            if (model == null) {
                binding.imStatus.setVisibility(View.GONE);
                binding.imOutline.setImageResource(R.drawable.ic_camera_add);
                binding.imInline.setVisibility(View.GONE);
                binding.tvName.setText(mContext.getString(R.string.create_room));
            } else {
                binding.imStatus.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(model.getName()))
                    binding.tvName.setText(model.getName());
                if (!model.isActive()) {
                    itemView.setVisibility(View.GONE);
//                    ImageUtils.loadImage(binding.imOutline, model.getImage());
                    binding.imInline.setVisibility(View.GONE);
                } else {
                    itemView.setVisibility(View.VISIBLE);
                    binding.imInline.setVisibility(View.VISIBLE);
//                    binding.imOutline.setImageResource(R.drawable.bg_round_0084f0);
                    ImageUtils.loadImage(binding.imInline, model.getImage());
                }
            }

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(getAdapterPosition());
            });
        }
    }
}
