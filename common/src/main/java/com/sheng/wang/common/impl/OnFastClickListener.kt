package com.sheng.wang.common.impl

import android.view.View

/**
 * 快速点击限制
 */
abstract class OnFastClickListener @JvmOverloads constructor(private var interval: Long = 1000L) : View.OnClickListener {
    private var mLastClickTime: Long = 0

    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastClickTime > interval) {
            // 经过了足够长的时间，允许点击
            onClickView(v)
            mLastClickTime = currentTime
        }
    }

    protected abstract fun onClickView(v: View)
}