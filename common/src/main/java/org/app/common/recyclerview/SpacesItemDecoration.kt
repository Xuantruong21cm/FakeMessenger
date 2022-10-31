package org.app.common.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State


/**
 * Created by Hoang Dep Trai on 18/04/2022.
 */

class SpacesItemDecoration(
    private val top: Int? = null,
    private val right: Int? = null,
    private val bottom: Int? = null,
    private val left: Int? = null
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        left?.let { outRect.left = it }
        right?.let { outRect.right = it }
        bottom?.let { outRect.bottom = it }

        top?.let { outRect.top = it }
    }
}