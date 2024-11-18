package com.sheng.wang.common.base

import androidx.multidex.MultiDexApplication

/**
 * 基础application
 */
@Deprecated("app自己继承MultiDexApplication实现")
abstract class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: BaseApplication? = null
            private set
    }
}