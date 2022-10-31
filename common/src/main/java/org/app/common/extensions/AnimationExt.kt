package org.app.common.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.annotation.IdRes
import androidx.constraintlayout.motion.widget.MotionLayout
import org.app.common.utils.listener.SimpleMotionLayoutListener
import kotlinx.coroutines.suspendCancellableCoroutine

fun View.scaleXY(
    scale: Float, duration: Long, needToStartNow: Boolean = false, onEnd: (() -> Unit)? = null
): AnimatorSet {
    val animatorX = ObjectAnimator.ofFloat(this, "scaleX", scale).apply { setDuration(duration) }
    val animatorY = ObjectAnimator.ofFloat(this, "scaleY", scale).apply { setDuration(duration) }
    return AnimatorSet().apply {
        play(animatorX).with(animatorY)
        this.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onEnd?.invoke()
            }
        })
        if (needToStartNow) start()
    }
}

fun View.scaleInAndScaleOut(
    duration: Long, scaleValue: Float = 1.02f, needToStartNow: Boolean = true
): AnimatorSet {
    return AnimatorSet().apply {
        play(scaleXY(scaleValue, duration / 2)).before(scaleXY(1f, duration / 2))
        if (needToStartNow) start()
    }
}

fun View.alphaAnimate(
    alpha: Float, duration: Long, needToStartNow: Boolean = false
): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "alpha", alpha).apply {
        this.duration = duration
        if (needToStartNow) start()
    }
}

fun View.animateMoveY(y: Float, duration: Long): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "translationY", y).apply { this.duration = duration }
}

fun View.disappearViaY(orgTransY: Float? = null, goneWhenEnd: Boolean? = true): AnimatorSet {
    return AnimatorSet().apply {
        play(alphaAnimate(0f, 200)).with(animateMoveY((orgTransY ?: translationY) + 50, 200))
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = if (goneWhenEnd == true) View.GONE else View.INVISIBLE
            }
        })
        start()
    }
}

fun View.appearViaY(orgTransY: Float? = null): AnimatorSet {
    return AnimatorSet().apply {
        orgTransY?.let { translationY = it + 50 }
        play(alphaAnimate(1f, 200)).with(animateMoveY(orgTransY ?: (translationY - 50), 200))
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                visibility = View.VISIBLE
            }
        })
        start()
    }
}

fun View.rotateLoopLikeWindChimes(toAngle: Float): AnimatorSet {
    post {
        this.pivotY = -measuredHeight / 50f
        this.pivotX = measuredWidth / 2f
    }
    return AnimatorSet().apply {
        play(ObjectAnimator.ofFloat(
            this@rotateLoopLikeWindChimes, "rotation", toAngle, -toAngle
        ).apply {
            duration = 2000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }).after(
            rotateTo(toAngle, 1000)
        )
        start()
    }
}

fun View.rotateTo(toAngle: Float, duration: Long): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "rotation", toAngle).apply {
        this.duration = duration
    }
}

fun View.backToOriginRotation(): AnimatorSet {
    post {
        this.pivotY = -measuredHeight / 50f
        this.pivotX = measuredWidth / 2f
    }
    clearAnimation()
    return AnimatorSet().apply {
        play(rotateTo(0f, 500L))
        start()
    }
}

fun View.moveAnimateViewTo(
    x: Float,
    y: Float,
    duration: Long? = null,
    needToStartNow: Boolean = true,
    onEnd: (() -> Unit)? = null
): AnimatorSet {
    val obX = ObjectAnimator.ofFloat(this, "translationX", x)
    val obY = ObjectAnimator.ofFloat(this, "translationY", y)
    return AnimatorSet().apply {
        play(obX).with(obY)
        duration?.let { this.duration = duration }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                onEnd?.invoke()
            }
        })
        if (needToStartNow) start()
    }
}

fun View.moveX(
    vararg x: Float,
    duration: Long? = null,
    needToStartNow: Boolean = true,
    onEnd: (() -> Unit)? = null
): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "translationX", *x).apply {
        duration?.let { this.duration = duration }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                onEnd?.invoke()
            }
        })
        if (needToStartNow) start()
    }
}

fun View.moveY(
    vararg y: Float,
    duration: Long? = null,
    needToStartNow: Boolean = true,
    onEnd: (() -> Unit)? = null
): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "translationY", *y).apply {
        duration?.let { this.duration = duration }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                onEnd?.invoke()
            }
        })
        if (needToStartNow) start()
    }
}

fun View.animateViewBySine(
    limitAnimateY: Float, offsetAnimate: Float, onEnd: ((View) -> Unit)? = null
) {
    AnimatorSet().apply {
        val duration = 1500L
        play(AnimatorSet().apply {
            play(moveX(offsetAnimate, needToStartNow = false, duration = duration / 10)).before(
                moveX(
                    offsetAnimate, -offsetAnimate, duration = duration / 10
                ).apply {
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                })
        }).with(scaleXY(
            duration = duration, scale = 0.2f, needToStartNow = true
        ) {
            onEnd?.invoke(this@animateViewBySine)
        }).with(
            moveY(-limitAnimateY, duration = duration)
        ).with(alphaAnimate(0f, duration, needToStartNow = true))
    }
}

suspend fun MotionLayout.awaitTransition(
    @IdRes startId: Int, @IdRes endId: Int, duration: Int? = null
) = suspendCancellableCoroutine<Unit> {
    var transitionListener: SimpleMotionLayoutListener? = null
    try {
        transitionListener = object : SimpleMotionLayoutListener() {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                it.resumeWith(Result.success(Unit))
                removeTransitionListener(transitionListener)
            }
        }
        setTransition(startId, endId)
        duration?.let { dur ->
            setTransitionDuration(dur)
        }
        progress = 0f
        addTransitionListener(transitionListener)
        transitionToEnd()
    } catch (e: Throwable) {
        it.resumeWith(Result.failure(e))
    }
}

suspend fun Animator.awaitAnimation() = suspendCancellableCoroutine<Unit> {
    var listener: AnimatorListenerAdapter? = null
    try {
        listener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                it.resumeWith(Result.success(Unit))
                removeListener(listener)
            }
        }
        addListener(listener)
        start()
    } catch (e: Throwable) {
        it.resumeWith(Result.failure(e))
    }
}