package com.sheng.wang.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * 基础自定义组件
 */
abstract class BaseCustomView @JvmOverloads constructor(context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ICustomView {
    abstract override fun initView(attrs: AttributeSet?)
    override fun show() {
        visibility = VISIBLE
    }

    override fun hide() {
        visibility = GONE
    }

    override fun onBack(): Boolean {
        return false
    }

    override val isShowing: Boolean
        get() = visibility == VISIBLE

    override fun initListener() {
    }

    /**
     * 生命周期
     */
    open fun resume() {
    }

    /**
     * 暂停
     */
    open fun pause() {
    }

    /**
     * 释放资源
     */
    open fun destroy() {
    }

    init {
        this.initView(attrs)
        this.initListener()
    }

}