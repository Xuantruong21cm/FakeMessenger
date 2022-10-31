package org.app.common.utils.listener

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior


/**
 * Created by Hoang Dep Trai on 05/23/2022.
 */

open class SimpleBottomSheetCallbackListener : BottomSheetBehavior.BottomSheetCallback() {
    override fun onStateChanged(bottomSheet: View, newState: Int) {
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
    }
}