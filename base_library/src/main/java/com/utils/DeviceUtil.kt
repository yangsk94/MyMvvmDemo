package com.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Configuration
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import com.BaseApplication
import com.BaseApplication.Companion

import java.io.FileReader
import java.io.InputStreamReader
import java.io.LineNumberReader
import java.io.Reader
import java.util.Locale

/**
 * Created by hxl on 2017/10/10.
 */

object DeviceUtil {

    /**
     * 获取macid
     */
    val macId: String
        get() = macAddress

    /**
     * 兼容4.x 与 6.0以上版本Mac地址获取
     */
    private// 去空格
    val macAddress: String
        get() {
            var str: String? = ""
            var macSerial = ""
            try {
                val pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address "
                )
                val ir = InputStreamReader(pp.inputStream)
                val input = LineNumberReader(ir)

                while (null != str) {
                    str = input.readLine()
                    if (str != null) {
                        macSerial = str.trim { it <= ' ' }
                        break
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            if ("" == macSerial) {
                try {
                    return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17)
                } catch (e: Exception) {
                    e.printStackTrace()

                }

            }
            return macSerial
        }

    /**
     * 设备序列号
     */
    val devSelNum: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(BaseApplication.context?.contentResolver, Settings.Secure.ANDROID_ID)

    /**
     * 手机品牌
     */
    val brand: String
        get() = android.os.Build.BRAND

    /**
     * 手机型号
     */
    val model: String
        get() = android.os.Build.MODEL

    /**
     * 获取设备分辨率
     */
    val devReso: String
        get() {
            val resolution: String
            val metrics = BaseApplication.context?.resources?.displayMetrics
            val screenWidth = metrics?.widthPixels
            val screenHeight = metrics?.heightPixels
            resolution = screenWidth.toString() + "x" + screenHeight
            return resolution
        }

    /**
     * 获取原生的版本名称
     */
    val appVersion: String
        get() = packageInfo?.versionName.toString()

    /**
     * 手机系统版本
     */
    val sdkVersion: String
        get() = android.os.Build.VERSION.RELEASE

    /**
     * 获取当前程序的包信息
     */
    private// getPackageName()是你当前类的包名，0代表是获取版本信息
    val packageInfo: PackageInfo?
        get() {
            val packageManager = packageManager
            var packInfo: PackageInfo? = null
            try {
                packInfo = packageManager?.getPackageInfo(BaseApplication.context?.packageName, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return packInfo
        }

    private val packageManager: PackageManager?
        get() = BaseApplication.context?.packageManager

    /**
     * 获取当前的系统给的版本code
     */
    val versionCode: Int?
        get() = packageInfo?.versionCode

    @Throws(Exception::class)
    private fun loadFileAsString(fileName: String): String {
        val reader = FileReader(fileName)
        val text = loadReaderAsString(reader)
        reader.close()
        return text
    }

    @Throws(Exception::class)
    private fun loadReaderAsString(reader: Reader): String {
        val builder = StringBuilder()
        val buffer = CharArray(4096)
        var readLength = reader.read(buffer)
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength)
            readLength = reader.read(buffer)
        }
        return builder.toString()
    }

    // 判断设备有无拨打电话功能
    fun isIntentAvailable(intent: Intent): Boolean {
        val packageManager = packageManager
        val list = packageManager?.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        if (list != null) {
            return list.size > 0
        }
        return false
    }

    fun checkPermission(context: Context, perName: String): Boolean {
        val permissionCheck = ContextCompat.checkSelfPermission(context, perName)
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 改变resource的默认设置语言
     */
    fun changeLocalLanguage(context: Context) {
        try {
            val configuration = context.resources.configuration
            Locale.setDefault(Locale.CHINESE)
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        } catch (ignored: Exception) {
        }

    }

    fun getScreenWidth(context: Context): Int {
        if (context is Activity) {
            val dm = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(dm)
            return dm.widthPixels
        }
        return 0
    }

    fun getScreenHeight(context: Context): Int {
        if (context is Activity) {
            val dm = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(dm)
            return dm.heightPixels
        }
        return 0
    }
}
