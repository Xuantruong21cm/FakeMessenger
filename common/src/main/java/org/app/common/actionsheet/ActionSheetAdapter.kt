package org.app.common.actionsheet

import android.R.attr
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.app.common.R
import org.app.common.databinding.ItemChooserActionBinding

internal class ActionSheetAdapter(
  private var itemClick: ((item: ActionSheet) -> Unit),
  private var supportSelection: Boolean,
  private var radioButtonSelectionColor: Int?
) :
  ListAdapter<ActionSheet, ActionSheetAdapter.ActionChooserViewHolder>(DIFF_CALLBACK) {

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ActionSheet>() {
      override
      fun areItemsTheSame(oldItem: ActionSheet, newItem: ActionSheet): Boolean =
        oldItem.type == newItem.type

      override
      fun areContentsTheSame(oldItem: ActionSheet, newItem: ActionSheet): Boolean =
        oldItem == newItem
    }
  }

  override
  fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ActionChooserViewHolder {
    val root = LayoutInflater.from(parent.context).inflate(R.layout.item_chooser_action, parent, false)
    return ActionChooserViewHolder(ItemChooserActionBinding.bind(root))
  }

  override
  fun onBindViewHolder(holder: ActionChooserViewHolder, position: Int) {
    holder.bind(position, getItem(position))
  }

  @SuppressLint("RestrictedApi")
  inner class ActionChooserViewHolder(private val itemBinding: ItemChooserActionBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var currentItem: ActionSheet

    init {
      itemBinding.llItem.setOnClickListener {
        itemClick.invoke(currentItem)
      }

      if (supportSelection) {
        itemBinding.rb.visibility = View.VISIBLE
      } else {
        itemBinding.rb.visibility = View.GONE
      }

      val colorStateList = ColorStateList(
        arrayOf(intArrayOf(-attr.state_checked), intArrayOf(attr.state_checked)),
        intArrayOf(
          Color.DKGRAY,
          radioButtonSelectionColor ?: Color.YELLOW
        )
      )
      itemBinding.rb.supportButtonTintList = colorStateList
    }

    fun bind(position: Int, item: ActionSheet) {
      currentItem = item

      itemBinding.tvItem.text = item.name
      itemBinding.rb.isChecked = item.isSelected

      if (currentList.size - 1 != position) {
        itemBinding.divider.visibility = View.VISIBLE
      } else {
        itemBinding.divider.visibility = View.GONE
      }
    }
  }
}