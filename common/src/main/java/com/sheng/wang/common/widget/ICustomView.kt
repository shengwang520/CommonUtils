package com.sheng.wang.common.widget

import android.util.AttributeSet

/**
 * 自定义布局接口
 */
interface ICustomView {
    /**
     * 初始化界面
     */
    fun initView(attrs: AttributeSet?)

    /**
     * 显示
     */
    fun show()

    /**
     * 隐藏
     */
    fun hide()

    /**
     * 物理返回键处理,在activity的onBackPressed方法中拦截
     *
     * @return true 拦截返回键
     */
    fun onBack(): Boolean

    /**
     * 界面是否显示
     */
    val isShowing: Boolean

    /**
     * 初始化监听,设置监听的按钮需要在initView方法中初始化
     */
    fun initListener()
}