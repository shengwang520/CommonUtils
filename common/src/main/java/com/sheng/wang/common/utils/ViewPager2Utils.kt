package com.sheng.wang.common.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Field

/**
 * ViewPager2修改
 */
object ViewPager2Utils {
    /**
     * 修改灵敏度
     */
    fun changeTouchSlop(viewPager: ViewPager2, value: Int = 8) {
        try {
            val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.isAccessible = true
            val recyclerView = recyclerViewField.get(viewPager) as RecyclerView
            val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
            touchSlopField.isAccessible = true
            val touchSlop = touchSlopField.get(recyclerView) as Int
            touchSlopField.set(recyclerView, touchSlop * value) //8 is empirical value
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}