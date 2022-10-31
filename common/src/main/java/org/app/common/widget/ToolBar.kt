package org.app.common.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.app.common.BaseInflateCustomView
import org.app.common.R
import org.app.common.databinding.LayoutToolbarViewBinding
import org.app.common.extensions.*
import kotlin.math.max


/**
 * Created by Hoang Dep Trai on 05/24/2022.
 */

class ToolBar : BaseInflateCustomView<LayoutToolbarViewBinding> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        applyAttrs(attrs)
    }

    private val menuItemSize = context.dimenInt(com.intuit.sdp.R.dimen._36sdp)

    private var elementPadding = context.dimenInt(com.intuit.sdp.R.dimen._8sdp)

    private val menuItemPadding = context.dimenInt(com.intuit.sdp.R.dimen._8sdp)

    private val menuItems by lazy { mutableListOf<MenuItem>() }

    private val menuItemViews by lazy { mutableListOf<View>() }


    fun addMenuItem(menuIconItem: MenuIconItem) {
        menuItems.add(menuIconItem)
        ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            setPadding(menuItemPadding, menuItemPadding, menuItemPadding, menuItemPadding)
            setOnClickListener {
                menuIconItem.action?.invoke()
            }
            if (applyTintForMenuItem)
                setTint(colorTint)
            menuItemViews.add(this)
            binding.lnAction.addView(this)
            displayMenuOrNot(menuIconItem)
        }
    }

    fun addMenuItem(menuItem: MenuTextItem) {
        menuItems.add(menuItem)
        TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
            setPadding(menuItemPadding, menuItemPadding, menuItemPadding, menuItemPadding)
            setOnClickListener {
                menuItem.action?.invoke()
            }
            setTextColor(menuItem.textColor)
            menuItemViews.add(this)
            binding.lnAction.addView(this)
            displayMenuOrNot(menuItem)
        }
    }

    private fun TextView.displayMenuOrNot(menuIconItem: MenuTextItem) {
        text = menuIconItem.text
        visibility = menuIconItem.visibility
        alpha = if (menuIconItem.isActive) 1f else 0.6f
        isEnabled = menuIconItem.isActive
    }

    private fun ImageView.displayMenuOrNot(menuIconItem: MenuIconItem) {
        setImageResource(menuIconItem.icon)
        visibility = menuIconItem.visibility
        alpha = if (menuIconItem.isActive) 1f else 0.6f
        isEnabled = menuIconItem.isActive
    }

    fun updateItemAt(position: Int, onUpdate: (MenuItem?) -> Unit) {
        onUpdate(if (position !in menuItems.indices) null else menuItems[position])
        if (position in menuItems.indices)
            menuItemViews[position].apply {
                val menuItem = menuItems[position]
                if (this is ImageView && menuItem is MenuIconItem) displayMenuOrNot(menuItem) else if (menuItem is MenuTextItem && this is TextView) displayMenuOrNot(
                    menuItem
                )
            }
    }

    var onBackListener: (() -> Unit)? = null

    var colorTint = Color.WHITE
        set(value) {
            binding.imgBack.setTint(value)
            binding.tvTitle.setTextColor(value)
            for (item in menuItemViews) {
                if (item is ImageView)
                    item.setTint(value)
            }
            field = value
        }

    var titleTextSize = context.dimenFloat(com.intuit.sdp.R.dimen._18sdp)
        set(value) {
            binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
            field = value
        }

    var backIcon: Drawable? = null
        set(value) {
            binding.imgBack.setImageDrawable(value)
            if (value == null) hideBack() else showBack()
            field = value
        }

    fun showBack() {
        binding.imgBack.show()
    }

    fun hideBack() {
        binding.imgBack.hide()
    }

    var title: CharSequence = ""
        set(value) {
            binding.tvTitle.text = value
            field = value
        }

    var titleAlignment: Int = -1
        set(value) {
            binding.tvTitle.gravity = value
            field = value
        }

    var applyTintForMenuItem = true
        set(value) {
            if (field == value) return
            field = value
            menuItemViews.forEach {
                if (it is ImageView)
                    it.colorFilter = null
            }
        }

    private fun updateGravityOfTitle() {
        binding.tvTitle.layoutParams.apply {
            if (this is ConstraintLayout.LayoutParams) {
                if (titleAlignment == Gravity.CENTER or Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL) {
                    val horizontal =
                        max(
                            binding.imgBack.measuredWidth,
                            binding.lnAction.measuredWidth
                        ) + elementPadding
                    setMargins(horizontal, elementPadding, horizontal, elementPadding)
                } else {
                    setMargins(
                        elementPadding + binding.imgBack.measuredWidth,
                        elementPadding,
                        elementPadding,
                        elementPadding
                    )
                }
                binding.tvTitle.layoutParams = this
            }
        }
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ToolBar)
        try {
            ta.getColor(R.styleable.ToolBar_tintColor, colorTint).takeIf { it != colorTint }?.let {
                colorTint = it
            }
            backIcon = ta.getDrawable(R.styleable.ToolBar_backIcon)
            title = ta.getString(R.styleable.ToolBar_title) ?: ""
            titleAlignment = ta.getInt(R.styleable.ToolBar_android_gravity, Gravity.START)
            titleTextSize = ta.getDimension(R.styleable.ToolBar_title_text_size, titleTextSize)
            elementPadding =
                ta.getDimensionPixelSize(R.styleable.ToolBar_padding_menu_item, elementPadding)
            applyTintForMenuItem = ta.getBoolean(R.styleable.ToolBar_apply_tint_for_menu_item, true)
        } finally {
            ta.recycle()
        }
    }

    init {
        doOnViewDrawnRepeat(removeCallbackBy = { false }, onDrawn = ::updateGravityOfTitle)
    }


    override fun LayoutToolbarViewBinding.onViewDrawn() {
        binding.imgBack.setOnClickListener {
            onBackListener?.invoke()
        }
    }

    open class MenuItem(
        var visibility: Int = View.VISIBLE,
        var isActive: Boolean = true,
        var action: (() -> Unit)? = null
    )

    class MenuIconItem(
        var icon: Int,
        visibility: Int = View.VISIBLE,
        isActive: Boolean = true,
        action: (() -> Unit)? = null
    ) : MenuItem(visibility, isActive, action)

    class MenuTextItem(
        var text: String,
        var textColor: Int = Color.BLACK,
        visibility: Int = View.VISIBLE,
        isActive: Boolean = true,
        action: (() -> Unit)? = null
    ) : MenuItem(visibility, isActive, action)

    data class MenuItemRequester(
        val menuIndex: Int,
        val menuIconItem: MenuItem
    )

}