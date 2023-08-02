package com.sheng.wang.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import java.util.UUID


/**
 * 安卓系统参数信息
 */
object DeviceUtils {
    private const val DATA_SYSTEM = "data_device_system"
    private const val DATA_UUID = "data_device_uuid"

    private fun sharedPreferences(context: Context?): SharedPreferences? {
        return context?.getSharedPreferences(DATA_SYSTEM, Context.MODE_PRIVATE)
    }

    /**
     * 保存数据
     */
    private fun saveData(context: Context?, value: String?) {
        sharedPreferences(context)?.edit()?.putString(DATA_UUID, value)?.apply()
    }

    /**
     * 获取数据
     */
    private fun getData(context: Context?): String? {
        return sharedPreferences(context)?.getString(DATA_UUID, "")
    }


    /**
     * 获取手机系统版本哈
     */
    val systemVersion: String
        get() = Build.VERSION.RELEASE

    /**
     * 获取手机型号
     */
    val systemModel: String
        get() = Build.MODEL


    /**
     * 获取设备唯一标识,优先使用androidId,再使用uuid
     */
    @SuppressLint("HardwareIds")
    fun androidId(context: Context?): String? {
        var uuid = getData(context)
        if (TextUtils.isEmpty(uuid)) {
            val androidId = Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
            uuid = if (TextUtils.isEmpty(androidId) || TextUtils.equals(androidId, "9774d56d682e549c")) { //部分手机可能生成同一androidId
                UUID.randomUUID().toString()
            } else {
                androidId
            }
            saveData(context, uuid)
        }
        return uuid
    }

}