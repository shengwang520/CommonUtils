package com.sheng.wang.common.pop

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.sheng.wang.common.R
import com.sheng.wang.common.compat.UiCompat
import com.sheng.wang.common.utils.MetricsUtils

/**
 * 基类popupWindow
 */
abstract class BasePopupWindow @JvmOverloads constructor(
    protected var context: Context?,
    private val isTransparent: Boolean = false,
    private val isAddAnim: Boolean = true, //是否需要添加动画
    private val animStyle: Int = R.style.PopupAnimation, //动画样式
    private val mWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT
) : PopupWindow(context) {
    var mView: View
    private var parentView: View? = null

    /**
     * 创建布局
     */
    protected abstract fun onCreateView(inflater: LayoutInflater): View

    /**
     * 布局创建后
     */
    protected abstract fun onViewCreate(view: View)

    private fun init() {
        this.contentView = mView
        if (isTransparent) {
            this.width = mWidth
            this.height = ViewGroup.LayoutParams.WRAP_CONTENT
            val dw = ColorDrawable(0x00000000)
            setBackgroundDrawable(dw)
        } else {
            this.width = MetricsUtils.getWidth(context!!)
            this.height = MetricsUtils.getHeight(context!!)
            val dw = ColorDrawable(0x66000000)
            setBackgroundDrawable(dw)
        }
        this.isFocusable = true
        if (isAddAnim) {
            this.animationStyle = animStyle
        }
        this.isOutsideTouchable = true
        this.isClippingEnabled = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            this.isAttachedInDecor = true
        }
        mView.setOnClickListener { dismiss() }
    }

    /**
     * 显示在底部
     */
    open fun show(v: View) {
        parentView = v
        showAtLocation(v, Gravity.BOTTOM, 0, UiCompat.getNavigationBarHeight(context!!))
    }

    /**
     * 初始化
     */
    init {
        val inflater = LayoutInflater.from(context)
        mView = this.onCreateView(inflater)
        init()
        this.onViewCreate(mView)
    }
}