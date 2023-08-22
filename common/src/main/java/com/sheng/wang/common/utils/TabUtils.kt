package com.sheng.wang.common.utils

import android.os.Build
import com.google.android.material.tabs.TabLayout

object TabUtils {
    /**
     * 屏蔽长点击事件
     */
    fun setTabLayoutLongClickable(tabLayout: TabLayout?) {
        if (tabLayout != null) {
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                setTabLongClickable(tab)
            }
        }
    }

    fun setTabLongClickable(tab: TabLayout.Tab?) {
        tab?.view?.isLongClickable = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tab?.view?.tooltipText = null
        }
    }
}