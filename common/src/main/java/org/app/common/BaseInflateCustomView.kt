package org.app.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.app.common.extensions.doOnViewDrawn
import org.app.common.extensions.layoutInflater
import org.app.common.extensions.registerEventBusBy
import org.app.common.extensions.unRegisterEventBus
import java.lang.reflect.ParameterizedType

abstract class BaseInflateCustomView<VB : ViewBinding> : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    protected val binding by lazy(::getViewBinding)

    protected var viewScope: CoroutineScope? = null

    protected open val needToSubscribeEventBus = false


    init {
        doOnViewDrawn {
            binding.onViewDrawn()
        }
    }

    private fun getViewBinding(): VB {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<*>
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return method.invoke(null, context.layoutInflater, this, true) as VB
    }

    protected open fun VB.onViewDrawn() {}

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        registerEventBusBy(needToSubscribeEventBus)
        viewScope = CoroutineScope(Dispatchers.IO)
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unRegisterEventBus()
        viewScope?.cancel()
    }
}