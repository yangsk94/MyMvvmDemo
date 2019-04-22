package com.utils

import android.text.TextUtils

import java.util.regex.Pattern

/**
 * Created by ysk on 2017/12/12.
 */

object ValidatorUtil {

    val REGEX_MOBILE = "^1\\d{10}$"
    val REGEX_CHINESE = "^[\u4e00-\u9fa5]+$"
    val REGEX_ID_CARD = "(^\\d{17}[0-9Xx]$)|(^\\d{15}$)"
    val REGEX_CAR = "^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$"
    val REGEX_TAX = "^[A-Z0-9]{15}$|^[A-Z0-9]{17}$|^[A-Z0-9]{18}$|^[A-Z0-9]{20}$"

    /**
     * 公司税号
     */
    fun isTaxNumber(number: String): Boolean {
        return Pattern.matches(REGEX_TAX, number)
    }

    /**
     * 1.常规车牌号：仅允许以汉字开头，后面可录入六个字符，由大写英文字母和阿拉伯数字组成。如：粤B12345；
     * 2.武警车牌：允许前两位为大写英文字母，后面可录入五个或六个字符，由大写英文字母和阿拉伯数字组成，其中第三位可录汉字也可录大写英文字母及阿拉伯数字，第三位也可空，如：WJ警00081、WJ京1234J、WJ1234X。
     * 3.最后一个为汉字的车牌：允许以汉字开头，后面可录入六个字符，前五位字符，由大写英文字母和阿拉伯数字组成，而最后一个字符为汉字，汉字包括“挂”、“学”、“警”、“军”、“港”、“澳”。如：粤Z1234港。
     * 4.新军车牌：以两位为大写英文字母开头，后面以5位阿拉伯数字组成。如：BA12345。
     */
    fun isCarNumber(number: String): Boolean {
        return Pattern.matches(REGEX_CAR, number)
    }

    /**
     * 校验手机号
     *
     * @return 校验通过返回true，否则返回false
     */
    fun isMobile(mobile: String): Boolean {
        return Pattern.matches(REGEX_MOBILE, mobile)
    }

    /**
     * 校验汉字
     *
     * @return 校验通过返回true，否则返回false
     */
    fun isChinese(chinese: String): Boolean {
        return Pattern.matches(REGEX_CHINESE, chinese)
    }

    /**
     * 校验身份证
     *
     * @return 校验通过返回true，否则返回false
     */
    fun isIDCard(idCard: String): Boolean {
        return Pattern.matches(REGEX_ID_CARD, idCard)
    }

    /**
     * 校验银行卡
     *
     * @return 校验通过返回true，否则返回false
     */
    fun isBankCard(bankCard: String): Boolean {
        if (TextUtils.isEmpty(bankCard)) {
            return false
        }
        return if (bankCard.length >= 9 && bankCard.length <= 27) {
            true
        } else {
            false
        }
    }

    fun addSpeaceForEdit(content: String): String {
        var content = content
        if (TextUtils.isEmpty(content)) {
            return ""
        }
        //去空格
        content = content.replace(" ".toRegex(), "")
        if (TextUtils.isEmpty(content)) {
            return ""
        }
        //卡号限制为16位
        if (content.length > 16) {
            content = content.substring(0, 16)
        }
        val newString = StringBuilder()
        for (i in 1..content.length) {
            //当为第4位时，并且不是最后一个第4位时
            //拼接字符的同时，拼接一个空格
            //如果在最后一个第四位也拼接，会产生空格无法删除的问题
            //因为一删除，马上触发输入框改变监听，又重新生成了空格
            if (i % 4 == 0 && i != content.length) {
                newString.append(content[i - 1] + " ")
            } else {
                //如果不是4位的倍数，则直接拼接字符即可
                newString.append(content[i - 1])

            }
        }
        return newString.toString()
    }
}
