package com.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 项目名称：xcbb_client_android
 * 类名称：ScreenUtils
 * 类描述：屏幕相关工具类
 * 创建人: Yangsk
 * 创建时间: 2017/6/27 17:36
 * 修改人：
 * 修改时间： 2017/6/27 17:36
 * 修改备注：
 */


object ScreenUtils {

    private var sStatusBarHeight = -1

    fun getScreenDpi(context: Context?): Float {
        return (context?.resources?.displayMetrics?.densityDpi ?: 0).toFloat()
    }

    fun getScreenHeight(context: Context?): Int {
        return context?.resources?.displayMetrics?.heightPixels ?: 0
    }

    fun getScreenWidth(context: Context?): Int {
        return context?.resources?.displayMetrics?.widthPixels ?: 0
    }

    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }

    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal, context.resources.displayMetrics
        ).toInt()
    }

    fun px2dp(context: Context, pxVal: Float): Float {
        val scale = context.resources.displayMetrics.density
        return pxVal / scale
    }

    fun px2sp(context: Context, pxVal: Float): Float {
        return pxVal / context.resources.displayMetrics.scaledDensity
    }

    @SuppressLint("PrivateApi")
    fun getStatusBarHeight(context: Context): Int {
        if (sStatusBarHeight > 0) {
            return sStatusBarHeight
        }
        val c: Class<*>?
        val obj: Any?
        val field: Field?
        val x: Int
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            x = NumberUtil.parseInt(field.get(obj).toString())
            sStatusBarHeight = context.resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            Logger.e(e)
        }

        return sStatusBarHeight
    }

    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = v.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
            v.layoutParams = p
        }
    }

    /**
     * 检查设备是否开启了虚拟键盘
     *
     * @return
     */
    private fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
        }

        return hasNavigationBar
    }

    fun setHideVirtualKey(activity: Activity) {
        if (!checkDeviceHasNavigationBar(activity)) {
            return
        }
        //保持布局状态
        var uiOptions =
        //                View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
        //全屏
        //                View.SYSTEM_UI_FLAG_FULLSCREEN|
        //隐藏导航栏
        //                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
        //                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            //布局位于状态栏下方
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions = uiOptions or 0x00001000
        } else {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
        //TODO 暂时屏蔽
        //        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    fun setHideVirtualKey(view: View) {
        if (!checkDeviceHasNavigationBar(view.context)) {
            return
        }
        //保持布局状态
        var uiOptions =
        //                View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
        //全屏
        //                View.SYSTEM_UI_FLAG_FULLSCREEN|
        //隐藏导航栏
        //                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
        //                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            //布局位于状态栏下方
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions = uiOptions or 0x00001000
        } else {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
        //TODO 暂时屏蔽
        //        view.setSystemUiVisibility(uiOptions);
    }
}
