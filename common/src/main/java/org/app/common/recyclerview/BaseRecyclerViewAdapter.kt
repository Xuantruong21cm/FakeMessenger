package org.app.common.recyclerview

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.app.common.databinding.ItemLoadMoreBinding
import org.app.common.extensions.*
import org.greenrobot.eventbus.EventBus
import java.util.*

abstract class BaseRecyclerViewAdapter<D>(
    protected val context: Context, private val orderLayout: Int = LayoutDirection.ARC
) : RecyclerView.Adapter<BaseViewHolder<*, D>>() {

    object LayoutDirection {
        const val ARC = 1
        const val DESC = -1
    }

    var onItemClick: ((D, Int) -> Unit)? = null

    var onDataChange: ((MutableList<D>, justModified: Boolean) -> Unit)? = null

    protected var adapterScope: CoroutineScope? = null

    val dataList = mutableListOf<D>()

    val indices
        get() = dataList.indices

    protected var recyclerView: RecyclerView? = null

    protected open val animateAlphaWhenReplaceData = true

    protected open val needSubscribeEventBus = false

    private var replaceDataJob: Job? = null

    private val onScrolled by lazy {
        object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager
                val conditionH =
                    layoutManager?.canScrollHorizontally() == true && recyclerView.canScrollHorizontally(
                        orderLayout
                    )
                val conditionV =
                    layoutManager?.canScrollVertically() == true && recyclerView.canScrollVertically(
                        orderLayout
                    )
                if (!conditionV && !conditionH) {
                    if (canLoadMore() && !showingLoadMore) showLoadMore()
                }
            }

        }
    }

    var canLoadMore = { true }

    private var showingLoadMore = false

    var onRequestMoreData: (() -> Unit)? = null

    open fun onShowLoadMore() {}

    open fun onHideLoadMore() {}

    fun findItem(onFind: (D) -> Boolean): D? {
        for (d in dataList) {
            if (onFind(d)) return d
        }
        return null
    }

    fun findIndex(onFind: (D) -> Boolean): Int {
        for ((index, d) in dataList.withIndex()) {
            if (onFind(d)) return index
        }
        return -1
    }

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(onScrolled)
        adapterScope = CoroutineScope(Dispatchers.IO)
        this.recyclerView = recyclerView
        if (needSubscribeEventBus && !EventBus.getDefault()
                .isRegistered(this)
        ) EventBus.getDefault().register(this)
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        adapterScope?.cancel()
        this.recyclerView = null
        unRegisterEventBus()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, D>, position: Int) {
        val pos = holder.adapterPosition
        val data = dataList[pos]
        holder.bindView(data, holder.adapterPosition)
        holder.itemClickListener = { d, p ->
            onItemClick?.invoke(d, p)
        }
    }

    override fun getItemCount() = dataList.size

    open fun replaceData(data: Collection<D>) {
        fun replace() {
            hideLoadMore()
            this.dataList.clear()
            this.dataList.addAll(data)
            notifyDataSetChanged()
            onDataChange?.invoke(dataList, false)
        }

        val hasDataPrevious = dataList.isNotEmpty()
        if (!hasDataPrevious || !animateAlphaWhenReplaceData) replace()
        else {
            replaceDataJob?.cancel()
            replaceDataJob = adapterScope?.launchMain {
                recyclerView?.alphaAnimate(
                    0f, MyCustomRecyclerView.DEFAULT_ANIMATED_DURATION / 2, false
                )?.awaitAnimation()
                replace()
            }
        }

    }

    private fun hideLoadMore() {
        onHideLoadMore()
        showingLoadMore = false
    }

    private fun showLoadMore() {
        onShowLoadMore()
        onRequestMoreData?.invoke()
        showingLoadMore = true
    }

    fun removeIf(condition: (D) -> Boolean) {
        remove(findItem(condition) ?: return)
    }

    open fun remove(item: D) {
        val index = findIndex { item == it }.takeIf { it > -1 } ?: return
        removeAt(index)
    }

    open fun removeAt(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataList.size - position)
        onDataChange?.invoke(dataList, true)
    }

    open fun addData(data: D) {
        hideLoadMore()
        dataList.add(data)
        notifyItemInserted(dataList.lastIndex)
        onDataChange?.invoke(dataList, true)
    }

    open fun addAll(data: Collection<D>) {
        hideLoadMore()
        dataList.addAll(data)
        notifyItemRangeInserted(dataList.size - data.size, data.size)
        onDataChange?.invoke(dataList, true)
    }

    open fun addData(position: Int, data: D) {
        hideLoadMore()
        dataList.add(position, data)
        notifyItemInserted(0)
        notifyItemRangeChanged(1, dataList.size - 1)
        onDataChange?.invoke(dataList, true)
    }

    open fun setItem(position: Int, data: D) {
        if (position !in dataList.indices) return
        dataList[position] = data
        notifyItemChanged(position)
    }

    open fun moveItem(from: Int, to: Int) {
        Collections.swap(dataList, from, to)
        this.notifyItemMoved(from, to)
        notifyItemChanged(from)
        notifyItemChanged(to)
    }

    fun notifyItemChanged(d: D) {
        val index = findIndex { d == it }.takeIf { it > -1 } ?: return
        notifyItemChanged(index)
    }

    protected fun createLoadMoreVH(parent: ViewGroup?) =
        LoadMoreViewHolder(ItemLoadMoreBinding.inflate(context.layoutInflater, parent, false))

    inner class LoadMoreViewHolder(vb: ItemLoadMoreBinding) :
        BaseViewHolder<ItemLoadMoreBinding, D>(vb) {

        override fun ItemLoadMoreBinding.bindView(data: D, position: Int) {

        }

    }
}