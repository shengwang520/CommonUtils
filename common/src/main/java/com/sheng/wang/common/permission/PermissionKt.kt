package com.sheng.wang.common.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.sheng.wang.common.R
import com.sheng.wang.common.base.BaseActivity

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
var onPermissionGranted: (() -> Unit)? = null

/**
 * 权限拒绝统一回调
 */
var onPermissionDenied: (() -> Unit)? = null


/**
 * 请求单个权限-基于BaseActivity实现
 * @param permission 要申请的权限
 * @param onGranted 同意
 * @param onDenied 拒绝
 */
fun Context?.requestPermission(
    permission: String?,
    onGranted: () -> Unit,
    onDenied: (() -> Unit)? = null
) {
    this?.let {
        if (it is BaseActivity) {
            onPermissionGranted = onGranted
            onPermissionDenied = onDenied
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
    onDenied: (() -> Unit)? = null
) {
    this?.let {
        if (it is BaseActivity) {
            onPermissionGranted = onGranted
            onPermissionDenied = onDenied
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
    onDenied: (() -> Unit)? = null
): ActivityResultLauncher<String> {
    val context = if (this is FragmentActivity) this else (this as? Fragment)?.activity
    val appSettingsLauncher = appSettingsLauncher()
    return registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if (result) {
            onGranted()
        } else {
            context?.let {
                Snackbar.make(it.window.decorView, R.string.c_permission_msg, Snackbar.LENGTH_LONG)
                    .setAction(R.string.c_permission_setting) {
                        appSettingsLauncher.launch(null)
                    }.show()
            }
            onDenied?.invoke()
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
    onDenied: (() -> Unit)? = null
): ActivityResultLauncher<Array<String>> {
    val context = if (this is FragmentActivity) this else (this as? Fragment)?.activity
    val appSettingsLauncher = appSettingsLauncher()
    return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap ->
        if (resultMap.containsValue(false)) {
            context?.let {
                Snackbar.make(it.window.decorView, R.string.c_permission_msg, Snackbar.LENGTH_LONG)
                    .setAction(R.string.c_permission_setting) {
                        appSettingsLauncher.launch(null)
                    }.show()
            }
            onDenied?.invoke()
        } else {
            onGranted()
        }
    }
}

/**
 * 跳转应用设置页的Launcher
 */
private fun ActivityResultCaller.appSettingsLauncher() = registerForActivityResult(LaunchAppSettingsContract()) {}

/**
 * 跳转设置页的协定
 */
private class LaunchAppSettingsContract : ActivityResultContract<Unit?, Unit>() {

    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", context.packageName, null))
    }

    override fun parseResult(resultCode: Int, intent: Intent?) = Unit
}

/**
 * 申请悬浮窗权限-基于BaseActivity实现
 */
fun Context?.requestWindowPermission() {
    this?.let {
        if (it is BaseActivity) {
            it.permissionWindowLauncher.launch(null)
        }
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
 * 跳转应用设置页的Launcher-悬浮窗
 */
fun ActivityResultCaller.appSettingsWindowLauncher() = registerForActivityResult(LaunchAppSettingsWindowContract()) {}

/**
 * 跳转设置页的协定-悬浮窗权限
 */
private class LaunchAppSettingsWindowContract : ActivityResultContract<Unit?, Unit>() {

    override fun createIntent(context: Context, input: Unit?): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                .setData(Uri.fromParts("package", context.packageName, null))
        } else {
            return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", context.packageName, null))
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) = Unit
}