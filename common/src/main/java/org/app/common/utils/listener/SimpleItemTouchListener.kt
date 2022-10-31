package org.app.common.utils.listener

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Hoang Dep Trai on 05/30/2022.
 */

abstract class SimpleItemTouchListener : RecyclerView.OnItemTouchListener {

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return rv.onTouchEvent(e)
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

}