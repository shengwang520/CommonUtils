package com.sheng.wang.common.helper

import android.util.Log
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity

/**
 * 开启安卓全屏适配
 */
fun FragmentActivity.openWindowInsets() {
    WindowCompat.setDecorFitsSystemWindows(window, false)//开启沉淀式 false开启，true关闭
}

/**
 * 隐藏底部导航栏
 */
fun FragmentActivity.hideNavigationBars() {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        hide(Type.navigationBars())
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

/**
 * 设置顶部状态栏颜色
 * @param isDark true黑色，false白色
 */
fun FragmentActivity.setStatusBarsColor(isDark: Boolean = false) {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        isAppearanceLightStatusBars = isDark
    }
}

/**
 * 获取页面状态栏，导航栏高度
 * @param bloc 返回状态栏，导航栏高度
 */
fun FragmentActivity.getSystemBarsHeight(bloc: (Int, Int) -> Unit) {
    window.decorView.setOnApplyWindowInsetsListener { v, insets ->
        ViewCompat.getRootWindowInsets(v)?.let {
            val statusBar = it.getInsets(Type.statusBars()).top
            val navigationBar = it.getInsets(Type.navigationBars()).bottom
            Log.d("App log", "fragmentActivity height getSystemBarsHeight: $statusBar |$navigationBar")
            bloc(statusBar, navigationBar)
        } ?: run {
            bloc(0, 0)
        }
        insets
    }
}