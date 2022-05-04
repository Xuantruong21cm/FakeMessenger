package com.lubuteam.fakemessenger.fakeconversation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.fakemessenger.fakeconversation.R;
import com.lubuteam.fakemessenger.fakeconversation.databinding.ItemActiveStatusBinding;
import com.lubuteam.fakemessenger.fakeconversation.model.ConversationModel;
import com.lubuteam.fakemessenger.fakeconversation.utils.ImageUtils;

import java.util.List;

public class ActiveStatusAdapter extends BaseRecyclerAdapter<ConversationModel, ActiveStatusAdapter.ViewHolder> {

    public ActiveStatusAdapter(Context context, List<ConversationModel> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ActiveStatusAdapter.ViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public ActiveStatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_active_status, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemActiveStatusBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemActiveStatusBinding.bind(itemView);
        }

        public void bindData(ConversationModel model) {
            if (model == null)
                return;
            binding.imStatus.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(model.getName()))
                binding.tvName.setText(model.getName());
            boolean isActive = model.isActive();
            binding.swStatus.setChecked(isActive);
            if (!isActive) {
                ImageUtils.loadImage(binding.imOutline, model.getImage());
                binding.imInline.setVisibility(View.GONE);
            } else {
                binding.imInline.setVisibility(View.VISIBLE);
                binding.imOutline.setImageResource(R.drawable.bg_round_0084f0);
                ImageUtils.loadImage(binding.imInline, model.getImage());
            }

            binding.swStatus.setOnClickListener(v -> {
                list.get(getAdapterPosition()).setActive(!isActive);
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}
