package com.sheng.wang.common.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.sheng.wang.common.R

/**
 * android 6.0+ 权限管理工具
 */
object DexterPermissionsUtil {
    const val PERMISSION_CAMERA = Manifest.permission.CAMERA //相机权限
    const val PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO //录音权限

    //相机，录音权限
    val CAMERA_AUDIO_PERMISSION = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

    //文件读权限
    val READ_MEDIA_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    /**
     * 请求单个权限
     */
    fun requestPermission(context: Context?, permission: String?, callBack: CallBack?) {
        if (context != null) {
            if (checkPermission(context, permission)) {
                callBack?.showPermissionGranted(permission)
                return
            }
        }
        Dexter.withContext(context)
            .withPermission(permission)
            .withListener(CompositePermissionListener(SamplePermissionListener(callBack), createDialogPermissionListener(context)))
            .withErrorListener {
                callBack?.showPermissionError(it.toString())
            }
            .check()
    }


    /**
     * 请求某个功能需要的多个权限
     */
    fun requestPermissions(context: Context?, callBack: CallBack?, vararg permissions: String?) {
        if (context != null) {
            if (checkPermissions(context, permissions)) {
                callBack?.showPermissionGranted(permissions.toString())
                return
            }
        }

        Dexter.withContext(context)
            .withPermissions(*permissions)
            .withListener(SampleMultiplePermissionListener(callBack, createDialogMultiplePermissionListener(context)))
            .withErrorListener {
                callBack?.showPermissionError(it.toString())
            }
            .check()
    }

    /**
     * 创建拒绝弹出提示
     */
    private fun createDialogPermissionListener(context: Context?): DialogOnDeniedPermissionListener {
        return DialogOnDeniedPermissionListener.Builder.withContext(context)
            .withTitle(R.string.c_permission_title)
            .withMessage(R.string.c_permission_msg)
            .withButtonText(R.string.c_permission_setting) {
                go2OpenPermission(context)
            }
            .build()
    }

    /**
     * 创建拒绝弹出提示
     */
    private fun createDialogMultiplePermissionListener(context: Context?): DialogOnAnyDeniedMultiplePermissionsListener {
        return DialogOnAnyDeniedMultiplePermissionsListener.Builder.withContext(context)
            .withTitle(R.string.c_permission_title)
            .withMessage(R.string.c_permission_msg)
            .withButtonText(R.string.c_permission_setting) {
                go2OpenPermission(context)
            }
            .build()
    }

    /**
     * 检查是否有权限
     */
    fun checkPermission(activity: Context, permission: String?): Boolean {
        return ActivityCompat.checkSelfPermission(activity, permission ?: "") == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查是否有权限
     */
    fun checkPermissions(activity: Context, permissions: Array<out String?>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission ?: "") != PackageManager.PERMISSION_GRANTED) {
                //当有1个没有权限时,则判断无权限
                return false
            }
        }
        return true
    }

    /**
     * 取设置开启权限
     */
    private fun go2OpenPermission(context: Context?) {
        val myAppSettings = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + context?.packageName)
        )
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(myAppSettings)
    }

    interface CallBack {
        /**
         * 同意用户申请的权限
         */
        fun showPermissionGranted(permission: String?)

        /**
         * 拒绝用户申请的权限
         */
        fun showPermissionDenied(permission: String?, permanentlyDenied: Boolean)

        /**
         * 申请权限异常
         */
        fun showPermissionError(error: String?)
    }

    /**
     * 简单接口实现
     */
    open class SimpleCallBack : CallBack {
        override fun showPermissionGranted(permission: String?) {
        }

        override fun showPermissionDenied(permission: String?, permanentlyDenied: Boolean) {
        }

        override fun showPermissionError(error: String?) {
        }
    }
}