package com.sheng.wang.common.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView

/**
 * 宽高一样的布局，以宽为准,在xml中作为容器使用
 */
class HVSameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : MaterialCardView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec) //让宽高一样
    }
}