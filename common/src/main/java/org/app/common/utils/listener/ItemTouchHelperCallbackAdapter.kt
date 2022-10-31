package org.app.common.utils.listener

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.app.common.recyclerview.BaseViewHolder

/**
 * Created by HoangDepTrai on 28, July, 2022 at 2:55 PM
 */
open class ItemTouchHelperCallbackAdapter(
    private val itemTouchListener: ItemTouchListener,
    private val useSwipe: Boolean = false
) :
    ItemTouchHelper.Callback() {

    private var focusViewHolder: BaseViewHolder<*, *>? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {

        val layoutManager = recyclerView.layoutManager

        var isVertical = true
        when (layoutManager) {
            is LinearLayoutManager -> isVertical =
                layoutManager.orientation == LinearLayoutManager.VERTICAL
            is GridLayoutManager -> isVertical =
                layoutManager.orientation == LinearLayoutManager.VERTICAL
        }


        val vertical = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val horizontal = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val nonSwipe = ItemTouchHelper.ACTION_STATE_IDLE
        return if (isVertical) makeMovementFlags(
            vertical,
            if (useSwipe) horizontal else nonSwipe
        ) else makeMovementFlags(
            horizontal,
            if (useSwipe) vertical else nonSwipe
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        itemTouchListener.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        itemTouchListener.onSwipe(viewHolder.adapterPosition, direction)
    }

    abstract class ItemTouchListener {
        open fun onSwipe(position: Int, direction: Int) {}

        open fun onMove(from: Int, to: Int) {}
    }
}