package org.app.common.extensions

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import org.app.common.R
import org.app.common.utils.SafeClickListener
import java.io.File
import java.lang.reflect.ParameterizedType

fun View.show() {
  if (visibility == View.VISIBLE) return

  visibility = View.VISIBLE
  if (this is Group) {
    this.requestLayout()
  }
}

fun View.hide() {
  if (visibility == View.GONE) return

  visibility = View.GONE
  if (this is Group) {
    this.requestLayout()
  }
}

fun View.invisible() {
  if (visibility == View.INVISIBLE) return

  visibility = View.INVISIBLE
  if (this is Group) {
    this.requestLayout()
  }
}

@BindingAdapter("app:goneUnless")
fun View.goneUnless(visible: Boolean) {
  visibility = if (visible) View.VISIBLE else View.GONE
  if (this is Group) {
    this.requestLayout()
  }
}

fun ImageView.drawCircle(backgroundColor: String, borderColor: String? = null) {
  val shape = GradientDrawable()
  shape.shape = GradientDrawable.OVAL
  shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

  shape.setColor(Color.parseColor(backgroundColor))

  borderColor?.let {
    shape.setStroke(10, Color.parseColor(it))
  }

  background = shape
}

fun <VB : ViewBinding> Any.inflateViewBinding(
  inflater: LayoutInflater,
  parent: ViewGroup? = null,
  attachToParent: Boolean = false
): VB {
  val clazz =
    (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>
  return clazz.getMethod(
    "inflate",
    LayoutInflater::class.java,
    ViewGroup::class.java,
    Boolean::class.java
  ).invoke(null, inflater, parent, attachToParent) as VB
}

fun ImageView.setTint(@ColorRes id: Int) =
  setColorFilter(ContextCompat.getColor(context, id), PorterDuff.Mode.SRC_IN)

fun View.enable() {
  isEnabled = true
  alpha = 1f
}

fun ViewPager2.reduceDragSensitivity() {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView

    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop * 8)       // "8" was obtained experimentally
}

fun View.disable() {
  isEnabled = false
  alpha = 0.5f
}

fun View.showSnackBar(message: String, retryActionName: String? = null, action: (() -> Unit)? = null) {
  val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)

  action?.let {
    snackBar.setAction(retryActionName) {
      it()
    }
  }

  snackBar.show()
}

@BindingAdapter(value = ["app:loadImage", "app:progressBar"], requireAll = false)
fun ImageView.loadImage(imageUrl: String?, progressBar: ProgressBar?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    val request = ImageRequest.Builder(context)
      .data(imageUrl)
      .crossfade(true)
      .crossfade(400)
      .placeholder(R.color.backgroundGray)
      .error(R.drawable.bg_no_image)
      .target(
        onStart = { placeholder ->
          progressBar?.show()
          setImageDrawable(placeholder)
        },
        onSuccess = { result ->
          progressBar?.hide()
          setImageDrawable(result)
        }
      )
      .listener(onError = { request: ImageRequest, _: Throwable ->
        progressBar?.hide()
        setImageDrawable(request.error)
      })
      .build()

    ImageLoader(context).enqueue(request)
  } else {
    progressBar?.hide()

    setImageResource(R.drawable.bg_no_image)
  }
}

@BindingAdapter(value = ["app:loadCircleImage", "app:progressBar"], requireAll = false)
fun ImageView.loadCircleImage(imageUrl: String?, progressBar: ProgressBar?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    val request = ImageRequest.Builder(context)
      .data(imageUrl)
      .crossfade(true)
      .crossfade(400)
      .placeholder(R.color.backgroundGray)
      .error(R.drawable.bg_no_image)
      .transformations(
        CircleCropTransformation()
      )
      .target(
        onStart = { placeholder ->
          progressBar?.show()
          setImageDrawable(placeholder)
        },
        onSuccess = { result ->
          progressBar?.hide()
          setImageDrawable(result)
        }
      )
      .listener(onError = { request: ImageRequest, _: Throwable ->
        progressBar?.hide()
        setImageDrawable(request.error)
      })
      .build()

    ImageLoader(context).enqueue(request)
  } else {
    progressBar?.hide()

    load(R.drawable.bg_no_image) {
      crossfade(true)
      transformations(
        CircleCropTransformation()
      )
    }
  }
}

@BindingAdapter(value = ["app:loadRoundImage", "app:progressBar"], requireAll = false)
fun ImageView.loadRoundImage(imageUrl: String?, progressBar: ProgressBar?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    val request = ImageRequest.Builder(context)
      .data(imageUrl)
      .crossfade(true)
      .crossfade(400)
      .placeholder(R.color.backgroundGray)
      .error(R.drawable.bg_no_image)
      .transformations(
        RoundedCornersTransformation(
          resources.getDimension(R.dimen.dimen7)
        )
      )
      .target(
        onStart = { placeholder ->
          progressBar?.show()
          setImageDrawable(placeholder)
        },
        onSuccess = { result ->
          progressBar?.hide()
          setImageDrawable(result)
        }
      )
      .listener(onError = { request: ImageRequest, _: Throwable ->
        progressBar?.hide()
        setImageDrawable(request.error)
      })
      .build()

    ImageLoader(context).enqueue(request)
  } else {
    progressBar?.hide()

    load(R.drawable.bg_no_image) {
      crossfade(true)
      transformations(
        RoundedCornersTransformation(
          resources.getDimension(R.dimen.dimen7)
        )
      )
    }
  }
}

@BindingAdapter("load_drawable")
fun loadDrawable(imageView: ImageView, drawable: Drawable?) {
  imageView.setImageDrawable(drawable)
}

fun View.setSafeOnClickListener(onSafeClick: (View?) -> Unit) {
  val safeClickListener = SafeClickListener {
    onSafeClick(it)
  }
  setOnClickListener(safeClickListener)
}

fun View.doOnViewDrawn(removeCallbackBy: (() -> Boolean) = { true }, onDrawn: () -> Unit) {
  var global: ViewTreeObserver.OnGlobalLayoutListener? = null
  global = ViewTreeObserver.OnGlobalLayoutListener {
    if (removeCallbackBy()) {
      onDrawn()
      viewTreeObserver.removeOnGlobalLayoutListener(global)
    }
  }
  viewTreeObserver.addOnGlobalLayoutListener(global)
}

fun View.doOnViewDrawnRepeat(removeCallbackBy: (() -> Boolean) = { true }, onDrawn: () -> Unit) {
  var global: ViewTreeObserver.OnGlobalLayoutListener? = null
  global = ViewTreeObserver.OnGlobalLayoutListener {
    onDrawn()
    if (removeCallbackBy()) {
      viewTreeObserver.removeOnGlobalLayoutListener(global)
    }
  }
  viewTreeObserver.addOnGlobalLayoutListener(global)
}


//Extension by XuanTruong

private fun ImageView.glideLoadImage(requestBuilder: RequestBuilder<Drawable>, type: ImageViewType, cornerRadius: Float = 5f) {
  scaleType = ImageView.ScaleType.CENTER_CROP
  val option = requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL)
  when (type) {
    ImageViewType.CIRCLE -> {
      option.transition(DrawableTransitionOptions.withCrossFade())
        .transform(CircleCrop())
        .into(this)
    }
    ImageViewType.HRECT, ImageViewType.VRECT, ImageViewType.SQUARE -> {
      val opt = if (cornerRadius > 0) {
        RequestOptions().transform(CenterCrop(), RoundedCorners(dipToPix(cornerRadius, context).toInt()))
      } else {
        RequestOptions().transform(CenterCrop())
      }
      option.apply(opt).into(this)
    }
    else -> {
      requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL).transform(FitCenter()).into(this)
    }
  }
}

fun ImageView.loadImageAsset(path: String, type: ImageViewType, cornerRadius: Float = 5f) {
  glideLoadImage(Glide.with(this).load(Uri.parse("file:///android_asset/$path")), type, cornerRadius)
}

fun ImageView.loadImageFile(f: File, type: ImageViewType, cornerRadius: Float = 5f) {
  glideLoadImage(Glide.with(this).load(f), type, cornerRadius)
}

fun ImageView.loadImageFilePath(path: String, type: ImageViewType, cornerRadius: Float = 5f) {
  glideLoadImage(Glide.with(this).load(File(path)), type, cornerRadius)
}

fun ImageView.loadImageRes(resId : Int, type: ImageViewType, cornerRadius: Float = 5f) {
  glideLoadImage(Glide.with(this).load(resId), type, cornerRadius)
}

fun ImageView.loadImageUrl(url: String, type: ImageViewType, cornerRadius: Float = 5f) {
  glideLoadImage(Glide.with(this).load(url), type, cornerRadius)
}

fun ImageView.loadImageUri(url: Uri, type: ImageViewType, cornerRadius: Float = 5f) {
  glideLoadImage(Glide.with(this).load(url), type, cornerRadius)
}

fun ImageView.loadImageBitmap(bm: Bitmap, type: ImageViewType, cornerRadius: Float = 5f) {
  glideLoadImage(Glide.with(this).load(bm), type, cornerRadius)
}

enum class ImageViewType {
  NONE, CIRCLE, HRECT, VRECT, SQUARE
}