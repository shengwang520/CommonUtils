package com.sheng.wang.common.compat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat

/**
 * 页面跳转
 */
object AppCompat {
    /**
     * 跳转界面
     */
    fun startActivity(context: Context, intent: Intent) {
        ActivityCompat.startActivity(context, intent, null)
    }

    /**
     * 跳转界面
     */
    fun startActivities(context: Context, vararg intent: Intent) {
        ActivityCompat.startActivities(context, intent, null)
    }

    /**
     * 启动界面
     */
    fun startActivity(context: Context, intent: Intent, requestCode: Int) {
        if (context is Activity) {
            startActivity(context, intent, requestCode)
        } else {
            startActivity(context, intent)
        }
    }

    /**
     * 启动界面
     */
    fun startActivity(activity: Activity, intent: Intent, requestCode: Int) {
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null)
    }


    /**
     * 启动界面-共享元素动画
     * @param transitionName 共享元素
     */
    fun startActivity(context: Context, view: View, intent: Intent, transitionName: String) {
        if (context is Activity) {
            val option = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, transitionName)
            ActivityCompat.startActivity(context, intent, option.toBundle())
        } else {
            startActivity(context, intent)
        }
    }

    /**
     * 关闭界面
     */
    fun finishActivity(activity: Activity) {
        ActivityCompat.finishAfterTransition(activity)
    }
}