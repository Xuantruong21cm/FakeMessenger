package com.merryblue.fakemessenger.ui.intro.language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.merryblue.fakemessenger.R
import com.merryblue.fakemessenger.databinding.ItemLanguageBinding
import org.app.data.model.LanguageModel

class LanguageAdapter internal constructor(private var items: List<LanguageModel>,
                                           val itemClick: (LanguageModel) -> Unit) :
    RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var itemBinding: ItemLanguageBinding

        fun bind(obj: LanguageModel) {
            itemBinding.data = obj
            itemBinding.executePendingBindings()
        }

        init {
            itemBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLanguageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_language, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemBinding.root.setOnClickListener {
            itemClick(items[position])
        }
    }

    override fun getItemCount() = items.size
}