package com.utils

import android.text.TextUtils

import java.text.NumberFormat
import java.util.Locale

/**
 * 项目名称：xcbb_client_android
 * 类名称：NumberUtil
 * 类描述：
 * 创建人: Yangsk
 * 创建时间: 2017/6/12 17:17
 * 修改人：
 * 修改时间： 2017/6/12 17:17
 * 修改备注：
 */


object NumberUtil {
    /**
     * 使数字按最小保留小数位数格式化,3个为一组添加逗号
     *
     * @param num    需要格式化数字
     * @param digits 最小保留小数位数
     * @return
     */
    fun tractionDigits(num: Float, digits: Int): String {
        val nbf = NumberFormat.getInstance(Locale.US)
        nbf.minimumFractionDigits = digits
        nbf.maximumFractionDigits = digits
        nbf.isGroupingUsed = true
        return nbf.format(num.toDouble())
    }

    fun tractionDigits(num: Double, digits: Int): String {
        val nbf = NumberFormat.getInstance(Locale.US)
        nbf.minimumFractionDigits = digits
        nbf.maximumFractionDigits = digits
        nbf.isGroupingUsed = true
        return nbf.format(num)
    }

    fun trans10ThousandValueToShortWord(num: String, unit: String, limitBits: Int = 6): String {
        if (TextUtils.isEmpty(num)) {
            return "0"
        }
        if (num.length > limitBits) {
            val numSub = num.substring(0, num.length - 3)
            val numBefore = numSub.substring(0, numSub.length - 1)
            val numAfter = numSub.substring(numSub.length - 1)
            return "$numBefore.$numAfter$unit"
        } else {
            return num
        }
    }

    fun parseInt(value: String, defaultValue: Int = 0): Int {
        if (!TextUtils.isEmpty(value)) {
            try {
                return Integer.parseInt(value)
            } catch (ignored: Throwable) {
                throw CatchedException("value : $value can't parse to int !!!")
            }

        }
        return defaultValue
    }

    fun parseLong(value: String, radix: Int = 10): Long {
        if (!TextUtils.isEmpty(value)) {
            try {
                return java.lang.Long.parseLong(value, radix)
            } catch (ignored: Throwable) {
                throw CatchedException("value : $value can't parse to long !!!")
            }

        }
        return 0
    }
}
