package com.sheng.wang.common.base

import androidx.multidex.MultiDexApplication

/**
 * 基础application
 */
abstract class BassApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: BassApplication? = null
            private set
    }
}