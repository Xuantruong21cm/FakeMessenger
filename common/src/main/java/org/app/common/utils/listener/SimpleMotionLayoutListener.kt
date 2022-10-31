package org.app.common.utils.listener

import androidx.constraintlayout.motion.widget.MotionLayout


/**
 * Created by Hoang Dep Trai on 04/25/2022.
 */

open class SimpleMotionLayoutListener : MotionLayout.TransitionListener {
    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
    }
}