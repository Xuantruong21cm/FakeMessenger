package org.app.common.recyclerview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import org.app.common.R
import org.app.common.databinding.LayoutPlaceholderRecyclerviewBinding
import org.app.common.extensions.*

class MyCustomRecyclerView : FrameLayout {

    companion object {

        const val DEFAULT_ANIMATED_DURATION = 500L

    }

    constructor(context: Context) : super(context) {
        initView()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        recyclerView.setPadding(left, top, right, bottom)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MyCustomRecyclerView)
        try {
            useShimmer = ta.getBoolean(R.styleable.MyCustomRecyclerView_useShimmer, useShimmer)
            needToShowPlaceHolder = ta.getBoolean(
                R.styleable.MyCustomRecyclerView_rv_need_to_show_placeholder, needToShowPlaceHolder
            )
            var paddingEnd =
                ta.getDimensionPixelSize(R.styleable.MyCustomRecyclerView_rv_padding_end, 0)
            var paddingStart =
                ta.getDimensionPixelSize(R.styleable.MyCustomRecyclerView_rv_padding_start, 0)
            var paddingBottom =
                ta.getDimensionPixelSize(R.styleable.MyCustomRecyclerView_rv_padding_bottom, 0)
            var paddingTop =
                ta.getDimensionPixelSize(R.styleable.MyCustomRecyclerView_rv_padding_top, 0)

            ta.getDimensionPixelSize(R.styleable.MyCustomRecyclerView_rv_padding_horizontal, 0)
                .takeIf { it > 0 }?.apply {
                    paddingStart = this
                    paddingEnd = this
                }

            ta.getString(R.styleable.MyCustomRecyclerView_rv_placeholder_text)?.let {
                placeHolderText = it
            }

            ta.getDimensionPixelSize(R.styleable.MyCustomRecyclerView_rv_padding_vertical, 0)
                .takeIf { it > 0 }?.apply {
                    paddingTop = this
                    paddingBottom = this
                }
            ta.getDimensionPixelSize(R.styleable.MyCustomRecyclerView_rv_padding, 0)
                .takeIf { it > 0 }?.apply {
                    paddingTop = this
                    paddingBottom = this
                    paddingStart = this
                    paddingEnd = this
                }
            placeHolderBinding.tvPlaceHolder.setTextSize(
                TypedValue.COMPLEX_UNIT_PX, ta.getDimension(
                    R.styleable.MyCustomRecyclerView_rv_placeholder_text_size, context.dimenFloat(
                        com.intuit.sdp.R.dimen._14sdp
                    )
                )
            )
            recyclerView.isMotionEventSplittingEnabled =
                ta.getBoolean(R.styleable.MyCustomRecyclerView_rv_split_motion_events, true)

            recyclerView.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
            recyclerView.clipToPadding = ta.getBoolean(
                R.styleable.MyCustomRecyclerView_rv_clip_to_padding, recyclerView.clipToPadding
            )
        } finally {
            ta.recycle()
        }
        initView()
    }

    private val recyclerView by lazy(::createRecyclerView)

    private val progressBarView by lazy(::createLoadingView)

    private val placeHolderBinding by lazy(::createPlaceholderView)

    private val loadingSize by lazy { dimenInt(com.intuit.sdp.R.dimen._40sdp) }

    private val dp16 by lazy { dimenInt(com.intuit.sdp.R.dimen._16sdp) }

    private var previousAdapter: BaseRecyclerViewAdapter<*>? = null

    private val loadingView: View
        get() = progressBarView


    var needToShowPlaceHolder = true
        set(value) {
            if (placeHolderBinding.root.visibility == VISIBLE) {
                hidePlaceHolder()
            }
            field = value
        }

    var onPlaceholderStateChangeListener: PlaceHolderStateChangeListener? = null

    var useShimmer = false

    var placeHolderText
        get() = placeHolderBinding.tvPlaceHolder.text
        set(value) {
            placeHolderBinding.tvPlaceHolder.text = value
        }

    fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        recyclerView.addItemDecoration(itemDecoration)
    }

    fun attachSnapHelper(snapHelper: SnapHelper) {
        snapHelper.attachToRecyclerView(recyclerView)
    }


    var hasFixedSize
        get() = recyclerView.hasFixedSize()
        set(value) = recyclerView.setHasFixedSize(value)

    var itemAnimator
        get() = recyclerView.itemAnimator
        set(value) {
            recyclerView.itemAnimator = value
        }

    fun addOnItemTouchListener(listener: RecyclerView.OnItemTouchListener) {
        recyclerView.addOnItemTouchListener(listener)
    }


    var adapter: BaseRecyclerViewAdapter<*>? = null
        set(value) {
            recyclerView.adapter = value?.apply {
                onDataChange = { data, justModified ->
                    if (data.isNotEmpty()) {
                        if (!justModified) {
                            animateDisplayRecyclerview()
                        }
                        hidePlaceHolder()
                    } else {
                        showPlaceHolder()
                    }
                }
            }
            if (value != null) previousAdapter = value
            field = value
        }

    var layoutManager: RecyclerView.LayoutManager? = null
        set(value) {
            recyclerView.layoutManager = value
            field = value
        }

    fun smoothScrollToPosition(position: Int) {
        recyclerView.smoothScrollToPosition(position)
    }

    override fun scrollBy(x: Int, y: Int) {
        recyclerView.scrollBy(x, y)
    }

    fun smoothScrollBy(x: Int, y: Int) {
        recyclerView.smoothScrollBy(x, y)
    }

    fun addOnScrollChangeListener(o: RecyclerView.OnScrollListener) {
        recyclerView.addOnScrollListener(o)
    }

    private fun initView() {
        showLoading()
    }

    private fun hidePlaceHolder() {
        recyclerView.show()
        if (loadingView.visibility == VISIBLE) {
            loadingView.alphaAnimate(0f, DEFAULT_ANIMATED_DURATION, true).apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        loadingView.hide()
                    }
                })
            }
        }
        placeHolderBinding.root.hide()
        onPlaceholderStateChangeListener?.onHidePlaceholder()
    }

    private fun animateDisplayRecyclerview() {
        recyclerView.alpha = 0f
        recyclerView.alphaAnimate(
            1f,
            if (recyclerView.visibility != VISIBLE) DEFAULT_ANIMATED_DURATION else DEFAULT_ANIMATED_DURATION / 2,
            true
        )
    }

    private fun showPlaceHolder() {
        if (needToShowPlaceHolder) placeHolderBinding.root.show()
        loadingView.hide()
        recyclerView.hide()
        onPlaceholderStateChangeListener?.onShowPlaceHolder()
    }

    fun addOnItemTouchCallback(callback: ItemTouchHelper.Callback) {
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun showLoading() {
        recyclerView.invisible()
        placeHolderBinding.root.invisible()
        loadingView.show()
        onPlaceholderStateChangeListener?.onShowLoading()
    }

    private fun createRecyclerView(): RecyclerView {
        return RecyclerView(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            visibility = GONE
            this@MyCustomRecyclerView.addView(this)
        }
    }

    private fun createLoadingView(): View {
        return ProgressBar(context).apply {
            layoutParams = LayoutParams(loadingSize, loadingSize).apply {
                gravity = Gravity.CENTER
            }
            hide()
            this@MyCustomRecyclerView.addView(this)
        }
    }

    private fun createPlaceholderView(): LayoutPlaceholderRecyclerviewBinding {
        return LayoutPlaceholderRecyclerviewBinding.inflate(
            context.layoutInflater
        ).apply {
            root.apply {
                val layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
                layoutParams.setMargins(dp16, dp16 / 2, dp16, dp16 / 2)
                this.layoutParams = layoutParams
                this@MyCustomRecyclerView.addView(this)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adapter = null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (adapter == null && previousAdapter != null) adapter = previousAdapter
    }

    open class PlaceHolderStateChangeListener {

        open fun onShowPlaceHolder() {}

        open fun onShowLoading() {}

        open fun onHidePlaceholder() {}

    }
}