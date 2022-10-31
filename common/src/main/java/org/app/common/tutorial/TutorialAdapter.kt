package org.app.common.tutorial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.app.common.R
import org.app.common.databinding.ItemTutorialBinding

internal class TutorialAdapter(
  private var titleColor: Int,
  private var contentColor: Int
) : ListAdapter<AppTutorial, TutorialAdapter.ImagesSliderViewHolder>(DIFF_CALLBACK) {

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AppTutorial>() {
      override
      fun areItemsTheSame(oldItem: AppTutorial, newItem: AppTutorial): Boolean =
        oldItem.hashCode() == newItem.hashCode()

      override
      fun areContentsTheSame(oldItem: AppTutorial, newItem: AppTutorial): Boolean =
        oldItem == newItem
    }
  }

  override
  fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ImagesSliderViewHolder {
    val root = LayoutInflater.from(parent.context).inflate(R.layout.item_tutorial, parent, false)
    return ImagesSliderViewHolder(ItemTutorialBinding.bind(root))
  }

  override
  fun onBindViewHolder(holder: ImagesSliderViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ImagesSliderViewHolder(private val itemBinding: ItemTutorialBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var currentItem: AppTutorial

    init {
      itemBinding.tvTitle.setTextColor(titleColor)
      itemBinding.tvContent.setTextColor(contentColor)
    }

    fun bind(item: AppTutorial) {
      currentItem = item

      itemBinding.tvTitle.text = item.title
      itemBinding.tvContent.text = item.content
      itemBinding.ivImage.setImageDrawable(item.image)
      if (item.animId != 0) {
        itemBinding.aniView.visibility = View.VISIBLE
        itemBinding.ivImage.visibility = View.GONE
        itemBinding.aniView.setAnimation(item.animId)
        if (item.animTitle.isNotBlank()) {
          itemBinding.tvAnimTitle.visibility = View.VISIBLE
          itemBinding.tvAnimTitle.text = item.animTitle
          itemBinding.aniVoteView.visibility = View.VISIBLE
        } else {
          itemBinding.tvAnimTitle.visibility = View.GONE
          itemBinding.aniVoteView.visibility = View.GONE
        }
      } else {
        itemBinding.aniView.visibility = View.GONE
        itemBinding.ivImage.visibility = View.VISIBLE
      }
    }
  }
}