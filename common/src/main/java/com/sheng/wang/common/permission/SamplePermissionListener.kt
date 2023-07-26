package com.sheng.wang.common.permission

import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener

class SamplePermissionListener internal constructor(private val mCallBack: DexterPermissionsUtil.CallBack?) :
    BasePermissionListener() {
    override fun onPermissionGranted(response: PermissionGrantedResponse) {
        mCallBack?.showPermissionGranted(response.permissionName)
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse) {
        mCallBack?.showPermissionDenied(response.permissionName, response.isPermanentlyDenied)
    }
}