package com.sheng.wang.common.compat

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 * ui一体化状态栏
 */
object UiCompat {
    /**
     * 沉浸式状态栏,在setContentView前调用
     * @param dark 状态栏颜色是否为黑色 true黑色 false白色
     */
    fun transparentStatusBar(activity: Activity, dark: Boolean = true) {
        //去掉半透明的可能性
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //可以设置系统栏的背景色
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor = Color.TRANSPARENT
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        setAndroidNativeLightStatusBar(activity, dark)
    }

    /**
     * 设置状态栏的显示隐藏
     */
    fun fullscreen(activity: Activity, enable: Boolean) {
        val lp = activity.window.attributes
        if (enable) { //显示状态栏
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            activity.window.attributes = lp
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else { //隐藏状态栏
            lp.flags = lp.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            activity.window.attributes = lp
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    /**
     * 设置view的距上的顶部的距离
     */
    fun setMarginTopViewParams(activity: Activity, view: View) {
        val params = view.layoutParams
        when (params) {
            is CollapsingToolbarLayout.LayoutParams -> {
                params.topMargin = getStatusBarHeight(activity)
            }

            is RelativeLayout.LayoutParams -> {
                params.topMargin = getStatusBarHeight(activity)
            }

            is LinearLayout.LayoutParams -> {
                params.topMargin = getStatusBarHeight(activity)
            }

            is ConstraintLayout.LayoutParams -> {
                params.topMargin = getStatusBarHeight(activity)
            }
        }
        view.layoutParams = params
    }

    /**
     * 获取状态栏的高度
     */
    fun getStatusBarHeight(activity: Context): Int {
        var result = 0
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = activity.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * Desc: 获取虚拟按键高度 放到工具类里面直接调用即可
     */
    fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        if (isNavigationBarExist(context)) {
            val res = context.resources
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    /**
     * 判断是否存在导航栏
     * 需要再activity onOpenCamera 中添加一下代码判断才生效
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     * window.navigationBarColor = ContextCompat.getColor(this, R.color.color_white)
     * }
     */
    private fun isNavigationBarExist(context: Context): Boolean {
        val NAVIGATION = "navigationBarBackground"
        if (context is Activity) {
            val vp = context.window.decorView as ViewGroup
            for (i in 0 until vp.childCount) {
                vp.getChildAt(i).context.packageName
                if (vp.getChildAt(i).id != -1 && NAVIGATION == context.getResources().getResourceEntryName(vp.getChildAt(i).id)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 设置状态栏文案颜色
     */
    fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    const val VIVO_NOTCH = 0x00000020 //是否有刘海

    /**
     * 全屏适配刘海方案
     */
    fun setActivityFullLiuHai(activity: Activity) {
        // 设置页面全屏显示 P适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val window = activity.window
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            // 设置页面延伸到刘海区显示
            window.attributes = lp
        } else {
            if (hasNotchInScreen(activity)) {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                transparentStatusBar(activity)
            }
        }
    }

    /**
     * 是否有刘海屏
     */
    fun hasNotchInScreen(activity: Activity): Boolean {
        // android  P 以上有标准 API 来判断是否有刘海屏
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val displayCutout = activity.window.decorView.rootWindowInsets.displayCutout
                // 说明有刘海屏
                displayCutout != null
            } else {
                // 通过其他方式判断是否有刘海屏  目前官方提供有开发文档的就 小米，vivo，华为（荣耀），oppo
                val manufacturer = Build.MANUFACTURER
                if (TextUtils.isEmpty(manufacturer)) {
                    false
                } else if (manufacturer.equals("HUAWEI", ignoreCase = true)) {
                    hasNotchAtHuawei(activity)
                } else if (manufacturer.equals("xiaomi", ignoreCase = true)) {
                    hasNotchAtMiui(activity)
                } else if (manufacturer.equals("oppo", ignoreCase = true)) {
                    hasNotchAtOPPO(activity)
                } else if (manufacturer.equals("vivo", ignoreCase = true)) {
                    hasNotchAtVivo(activity)
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 判断华为是否有刘海屏
     */
    fun hasNotchAtHuawei(context: Context): Boolean {
        var ret = false
        try {
            val classLoader = context.classLoader
            val HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
            ret = get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret
    }

    /**
     * 判断vivo是否有刘海屏
     */
    fun hasNotchAtVivo(context: Context): Boolean {
        var ret = false
        try {
            val classLoader = context.classLoader
            val FtFeature = classLoader.loadClass("android.util.FtFeature")
            val method = FtFeature.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            ret = method.invoke(FtFeature, VIVO_NOTCH) as Boolean
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret
    }

    /**
     * 判断oppo是否有刘海屏
     */
    fun hasNotchAtOPPO(context: Context): Boolean {
        val ret = context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
        return ret
    }

    /**
     * 判断miui是否有刘海屏
     */
    fun hasNotchAtMiui(context: Context): Boolean {
        var ret = false
        try {
            val classLoader = context.classLoader
            val FtFeature = classLoader.loadClass("android.os.SystemProperties")
            val get = FtFeature.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            ret = get.invoke(FtFeature, "ro.miui.notch", 0) as Int == 1
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret
    }
}