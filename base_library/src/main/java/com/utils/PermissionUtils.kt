package com.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker

/**
 * 项目名称：xcbb_client_android
 * 类名称：PermissionUtils
 * 类描述：
 * 创建人: Yangsk
 * 创建时间: 2017/6/12 15:53
 * 修改人：
 * 修改时间： 2017/6/12 15:53
 * 修改备注：
 */


object PermissionUtils {

    val CODE_CAMERA = 0x1111//相机权限

    val CODE_READ_PHONE_STATE = 0x1112

    val CODE_READ_WRITE_EXTERNAL_STORAGE = 0x1113

    val CODE_AUDIO = 0x1114//录音权限

    val CODE_PHONE = 0x1115//电话权限

    val LOCATIONGPS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE
    )

    fun hasLocationPermission(context: Context?): Boolean {
        if (null == context) {
            return false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val a = PermissionChecker.PERMISSION_GRANTED != PermissionChecker.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            val b = PermissionChecker.PERMISSION_GRANTED != PermissionChecker.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (a && b) {
                return false
            }
        }
        return true
    }

    /**
     * 申请权限
     *
     * @param activity
     * @param Permissions
     * @param requestCode
     */
    fun getPermission(activity: Activity, Permissions: Array<String>, requestCode: Int) {
        if (!hasPermission(activity, *Permissions)) {
            requestPermission(activity, requestCode, *Permissions)
        }
    }

    /**
     * 判断是否拥有权限
     *
     * @param permissions
     * @return
     */
    fun hasPermission(activity: Activity, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 请求权限
     */
    fun requestPermission(activity: Activity, code: Int, vararg permissions: String) {
        ActivityCompat.requestPermissions(activity, permissions, code)
    }

    fun requestHomePagePermission(activity: Activity?) {
        if (null == activity) {
            return
        }
        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (!hasPermission(activity, *PERMISSIONS)) {
            getPermission(activity, PERMISSIONS, PermissionUtils.CODE_READ_PHONE_STATE)
        }
    }

    fun requestLivePagePermission(activity: Activity?): Boolean {
        if (null == activity) {
            return false
        }
        val PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (!hasPermission(activity, *PERMISSIONS)) {
            getPermission(activity, PERMISSIONS, PermissionUtils.CODE_CAMERA)
            return true
        }
        return false
    }
}