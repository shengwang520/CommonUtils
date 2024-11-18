package com.sheng.wang.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * viewPager view 适配器
 */
open class BasePagerAdapter<T : View>(private val views: List<T>) : PagerAdapter() {
    override fun getCount(): Int {
        return views.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = views[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }
}