package com.sheng.wang.common.permission

import android.text.TextUtils
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener

class SampleMultiplePermissionListener internal constructor(
    private val mCallBack: DexterPermissionsUtil.CallBack?,
    private val dialog: DialogOnAnyDeniedMultiplePermissionsListener? = null
) :
    BaseMultiplePermissionsListener() {
    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
        val permissions: MutableList<String> = ArrayList()
        if (report.areAllPermissionsGranted()) {
            for (response in report.grantedPermissionResponses) {
                permissions.add(response.permissionName)
            }
            mCallBack?.showPermissionGranted(TextUtils.join(",", permissions))
        } else {
            for (response in report.deniedPermissionResponses) {
                permissions.add(response.permissionName)
            }
            mCallBack?.showPermissionDenied(TextUtils.join(",", permissions), false)
            dialog?.onPermissionsChecked(report)
        }
    }
}