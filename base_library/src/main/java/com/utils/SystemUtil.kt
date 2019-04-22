package com.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.text.TextUtils

import android.content.pm.PackageManager.GET_ACTIVITIES
import android.content.pm.PackageManager.GET_SERVICES

/**
 * Created by yangsk on 2017/6/8.
 */

object SystemUtil {

    fun isMainProcess(context: Context?): Boolean {

        if (null == context) {
            return false
        }

        val packageManager = context.packageManager
        val packageInfo: PackageInfo

        try {
            packageInfo = packageManager.getPackageInfo(context.packageName, GET_ACTIVITIES or GET_SERVICES)
        } catch (e: Exception) {
            return false
        }

        val mainProcess = packageInfo.applicationInfo.processName

        val myPid = android.os.Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var myProcess: ActivityManager.RunningAppProcessInfo? = null
        val runningProcess = activityManager.runningAppProcesses
        if (null != runningProcess) {
            for (process in runningProcess) {
                if (myPid == process.pid) {
                    myProcess = process
                    break
                }
            }
        }

        return if (null == myProcess) {
            false
        } else TextUtils.equals(mainProcess, myProcess.processName)

    }


}
