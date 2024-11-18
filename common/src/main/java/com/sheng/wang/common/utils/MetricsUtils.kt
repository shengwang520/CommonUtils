package com.sheng.wang.common.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.View.MeasureSpec

/**
 * 屏幕测量工具
 */
object MetricsUtils {
    /**
     * 获取控件的宽度
     *
     * @param offset 偏移量[dp]
     * @param count  每行个数
     */
    @JvmOverloads
    fun getWidth(context: Context, offset: Float = 0F, count: Int = 1): Int {
        val displayMetrics = getDisplayMetrics(context)
        return ((displayMetrics.widthPixels - dp2px(displayMetrics, offset)) / count).toInt()
    }

    private fun getDisplayMetrics(ctx: Context): DisplayMetrics {
        return ctx.resources.displayMetrics
    }

    /**
     * dp转px
     */
    fun dp2px(context: Context, dp: Float): Int {
        return dp2px(context.resources.displayMetrics, dp).toInt()
    }

    private fun dp2px(displayMetrics: DisplayMetrics?, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
    }

    /**
     * 获取控件的高度
     *
     * @param width 宽度
     * @param scale 宽高比
     */
    fun getHeight(width: Int, scale: Float): Int {
        return (width / scale).toInt()
    }

    fun measureChildren(parent: View) {
        parent.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
    }

    /**
     * 获取屏幕的高
     */
    fun getHeight(context: Context): Int {
        return getHeight(context, true)
    }

    /**
     * 获取屏幕的高
     */
    private fun getHeight(context: Context, real: Boolean): Int {
        val displayMetrics = getDisplayMetrics(context)
        if (real && context is Activity) {
            //适配刘海屏高度获取
            context.windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        }
        return displayMetrics.heightPixels
    }
}