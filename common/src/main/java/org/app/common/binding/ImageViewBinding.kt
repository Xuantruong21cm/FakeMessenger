package org.app.common.binding

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("url", "placeHolder", "errorHolder")
fun ShapeableImageView.setImage(url: String, placeHolder: Int, errorHolder: Int) {
    Glide.with(this.context)
        .load(url)
        .apply(
            RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .error(placeHolder)
                .placeholder(errorHolder)
                .dontAnimate()
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

@BindingAdapter(value = ["url", "placeHolder", "errorHolder", "isCircle"], requireAll = false)
fun AppCompatImageView.setImage(
    url: String?,
    placeHolder: Int,
    errorHolder: Int,
    isCircle: Boolean
) {
    if (isCircle) {
        val placeholder = BitmapFactory.decodeResource(
            resources,
            placeHolder
        )
        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, placeholder)
        circularBitmapDrawable.isCircular = true
        Glide.with(this.context)
            .load(url.orEmpty())
            .apply(
                RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                    .format(DecodeFormat.PREFER_ARGB_8888).error(circularBitmapDrawable)
                    .placeholder(circularBitmapDrawable).circleCrop().dontAnimate()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    } else {
        Glide.with(this.context)
            .load(url.orEmpty())
            .apply(
                RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                    .format(DecodeFormat.PREFER_ARGB_8888).error(errorHolder)
                    .placeholder(placeHolder).dontAnimate()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
}

@BindingAdapter(value = ["src", "placeHolder", "errorHolder", "isCircle"], requireAll = false)
fun AppCompatImageView.setImage(src: Int?, placeHolder: Int, errorHolder: Int, isCircle: Boolean) {
    if (isCircle) {
        val placeholder = BitmapFactory.decodeResource(
            resources,
            placeHolder
        )
        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, placeholder)
        circularBitmapDrawable.isCircular = true
        Glide.with(this.context)
            .load(src)
            .apply(
                RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                    .format(DecodeFormat.PREFER_ARGB_8888).error(circularBitmapDrawable)
                    .placeholder(circularBitmapDrawable).circleCrop().dontAnimate()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    } else {
        Glide.with(this.context)
            .load(src)
            .apply(
                RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                    .format(DecodeFormat.PREFER_ARGB_8888).dontAnimate()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
}

