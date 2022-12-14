package org.app.common.imageslider.fullscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.app.common.R
import org.app.common.databinding.ItemFullscreenSliderImageBinding
import org.app.common.imageslider.fullscreen.FullscreenImagesSliderAdapter.FullscreenImagesSliderViewHolder
import org.app.common.utils.loadSliderImage

internal class FullscreenImagesSliderAdapter : ListAdapter<String, FullscreenImagesSliderViewHolder>(DIFF_CALLBACK) {

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
      override
      fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem.hashCode() == newItem.hashCode()

      override
      fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
    }
  }

  override
  fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): FullscreenImagesSliderViewHolder {
    val root = LayoutInflater.from(parent.context).inflate(R.layout.item_fullscreen_slider_image, parent, false)
    return FullscreenImagesSliderViewHolder(ItemFullscreenSliderImageBinding.bind(root))
  }

  override
  fun onBindViewHolder(holder: FullscreenImagesSliderViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class FullscreenImagesSliderViewHolder(private val itemBinding: ItemFullscreenSliderImageBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var currentItem: String

    fun bind(item: String) {
      currentItem = item

      displayImage()
    }

    private fun displayImage() {
      itemBinding.ivImage.loadSliderImage(currentItem)
    }
  }
}