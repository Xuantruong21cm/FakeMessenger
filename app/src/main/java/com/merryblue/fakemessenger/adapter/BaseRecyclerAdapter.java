package com.merryblue.fakemessenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public Context mContext;
    public List<T> list = new ArrayList<>();
    public final LayoutInflater layoutInflater;

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public BaseRecyclerAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(int position, T item) {
        list.set(position, item);
        this.notifyItemChanged(position);
    }

    public void setData(T item) {
        this.notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void remove(int position) {
        list.remove(position);
        this.notifyItemRemoved(position);
    }

    public void add(int pos, T item) {
        list.add(pos, item);
        notifyItemInserted(pos);
    }

    public void add(T item) {
        list.add(item);
        notifyItemInserted(list.size() - 1);
    }


    public void addAll(List<T> listItems) {
        list.addAll(listItems);
        notifyDataSetChanged();
    }

    public T getItembyPostion(int position) {
        return list.get(position);
    }

    public abstract void onBindViewHolder(VH holder, final int position);

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
