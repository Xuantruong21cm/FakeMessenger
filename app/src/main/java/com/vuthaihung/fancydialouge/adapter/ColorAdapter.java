package com.vuthaihung.fancydialouge.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuthaihung.fancydialouge.R;
import com.vuthaihung.fancydialouge.databinding.ItemColorBinding;

import java.util.List;

public class ColorAdapter extends BaseRecyclerAdapter<String, ColorAdapter.ViewHolder> {

    private String colorSelected;

    public ColorAdapter(Context context, List<String> list) {
        super(context, list);
    }

    public void setColorSelected(String colorSelected) {
        this.colorSelected = colorSelected;
    }

    @Override
    public void onBindViewHolder(ColorAdapter.ViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_color, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemColorBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemColorBinding.bind(itemView);
        }

        public void bindData(String color) {
            if (!TextUtils.isEmpty(color))
                binding.imColor.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
            binding.imSelected.setVisibility(colorSelected.equals(color) ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(getAdapterPosition());
            });
        }
    }
}
