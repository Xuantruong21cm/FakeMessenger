package com.lubuteam.fakemessenger.fakeconversation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.fakemessenger.fakeconversation.R;
import com.lubuteam.fakemessenger.fakeconversation.databinding.ItemStickerBinding;
import com.bumptech.glide.Glide;

import java.util.List;

public class StickerAdapter extends BaseRecyclerAdapter<String, StickerAdapter.ViewHolder> {

    private ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void onStickerClickListener(int position, View view);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public StickerAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(StickerAdapter.ViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public StickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_sticker, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemStickerBinding stickerBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stickerBinding = ItemStickerBinding.bind(itemView);
        }

        public void bindData(String resName) {
            int drawableResourceId = mContext.getResources().getIdentifier(resName, "drawable", mContext.getPackageName());
            Glide.with(stickerBinding.imSticker)
                    .load(drawableResourceId)
                    .skipMemoryCache(false)
                    .into(stickerBinding.imSticker);

            itemView.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onStickerClickListener(getAdapterPosition(), itemView);
            });
        }
    }
}
