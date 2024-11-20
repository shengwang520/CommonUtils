package com.sheng.wang.common.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.sheng.wang.common.R
import com.sheng.wang.common.base.BaseActivity
import com.sheng.wang.common.helper.getNavigationBarHeight
import java.lang.ref.WeakReference

/**
 * 相机权限
 */
const val CAMERA = Manifest.permission.CAMERA

/**
 * 录音权限
 */
const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO

/**
 * 相机、录音权限
 */
val CAMERA_AUDIO = arrayOf(CAMERA, RECORD_AUDIO)

/**
 * 媒体文件阅读权限（图片/视频）
 */
val READ_MEDIA_IMAGES_VIDEO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
} else {
    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
}

/**
 * 图片阅读权限
 */
val READ_MEDIA_IMAGES = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    Manifest.permission.READ_MEDIA_IMAGES
} else {
    Manifest.permission.READ_EXTERNAL_STORAGE
}

/**
 * 音频阅读权限
 */
val READ_MEDIA_AUDIO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    Manifest.permission.READ_MEDIA_AUDIO
} else {
    Manifest.permission.READ_EXTERNAL_STORAGE
}

/**
 * 通知权限-13+才能直接申请
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
val POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS

/**
 * 权限同意统一回调
 */
var onPermissionGranted: WeakReference<(() -> Unit)>? = null

/**
 * 权限拒绝统一回调-返回true表示自己实现拦截后的逻辑，false使用默认实现
 */
var onPermissionDenied: WeakReference<(() -> Boolean)>? = null


/**
 * 请求单个权限-基于BaseActivity实现
 * @param permission 要申请的权限
 * @param onGranted 同意
 * @param onDenied 拒绝
 */
fun Context?.requestPermission(
    permission: String?,
    onGranted: () -> Unit,
    onDenied: (() -> Boolean)? = null
) {
    this?.let {
        if (it is BaseActivity) {
            onPermissionGranted = WeakReference(onGranted)
            onPermissionDenied = WeakReference(onDenied)
            it.permissionLauncher.launch(permission)
        }
    }
}

/**
 * 请求多个权限-基于BaseActivity实现
 * @param permissions 要申请的一组权限
 * @param onGranted 同意
 * @param onDenied 同意
 */
fun Context?.requestPermissions(
    permissions: Array<String>?,
    onGranted: () -> Unit,
    onDenied: (() -> Boolean)? = null
) {
    this?.let {
        if (it is BaseActivity) {
            onPermissionGranted = WeakReference(onGranted)
            onPermissionDenied = WeakReference(onDenied)
            it.permissionsLauncher.launch(permissions)
        }
    }
}

/**
 * 请求单个权限
 * @param onGranted 已授权
 * @param onDenied 已拒绝
 */
fun ActivityResultCaller.registerPermissionLaunch(
    onGranted: () -> Unit,
    onDenied: (() -> Boolean?)? = null
): ActivityResultLauncher<String> {
    val context = if (this is FragmentActivity) this else (this as? Fragment)?.activity
    return registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if (result) {
            onGranted()
        } else {
            onDenied?.invoke().let {
                if (it == null || it == false) {
                    context.showDeniedView()
                }
            }
        }
    }
}

/**
 * 请求单个权限
 * @param onGranted 已授权
 * @param onDenied 已拒绝
 */
fun ActivityResultCaller.registerPermissionsLaunch(
    onGranted: () -> Unit,
    onDenied: (() -> Boolean?)? = null
): ActivityResultLauncher<Array<String>> {
    val context = if (this is FragmentActivity) this else (this as? Fragment)?.activity
    return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap ->
        if (resultMap.containsValue(false)) {
            onDenied?.invoke().let {
                if (it == null || it == false) {
                    context.showDeniedView()
                }
            }
        } else {
            onGranted()
        }
    }
}

/**
 * 显示权限被拒绝弹窗
 */
private fun FragmentActivity?.showDeniedView() {
    this?.let {
        val view = it.findViewById<View>(android.R.id.content)
        Snackbar.make(view, R.string.c_permission_msg, Snackbar.LENGTH_LONG)
            .setAction(R.string.c_permission_setting) {
                this.startSettingPermission()
            }.apply {
                this.view.setPadding(0, 0, 0, it.getNavigationBarHeight())
            }.show()
    }
}


/**
 * 判断是否有悬浮窗权限
 * @return true 有权限 false 无权限
 */
fun Context?.isWindowPermission(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return Settings.canDrawOverlays(this)
    }
    return true
}

/**
 * 检查是否拥有权限
 * @param permission 需要判断的权限
 * @return true有 ，false没有
 */
fun Context?.isCheckPermission(permission: String): Boolean {
    this?.let {
        return ContextCompat.checkSelfPermission(it, permission) == PackageManager.PERMISSION_GRANTED
    }
    return false
}

/**
 * 启动手机app权限设置界面
 */
fun Context?.startSettingPermission() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        .setData(Uri.fromParts("package", this?.packageName, null))
    this?.startActivity(intent)
}

/**
 * 启动手机app悬浮窗权限设置界面
 */
fun Context?.startSettingWindow() {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            .setData(Uri.fromParts("package", this?.packageName, null))
    } else {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", this?.packageName, null))
    }
    this?.startActivity(intent)
}

/**
 * 启动手机设置wifi
 */
fun Context?.startSettingWifi() {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        this?.startActivity(Intent(Settings.Panel.ACTION_WIFI))
    } else {
        this?.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
    }
}