package com.helper

import android.text.TextUtils
import com.utils.Logger

import java.util.concurrent.ConcurrentHashMap

/**
 * 页面跳转的缓存辅助类，防止出现[android.os.TransactionTooLargeException] 的异常
 *
 *
 * 详见：[https://bugly.qq.com/v2/crash-reporting/errors/1400010551/16306/report?pid=1&search=TransactionTooLargeException%20&searchType=detail&bundleId=&channelId=&version=all&tagList=&start=0&date=all](https://bugly.qq.com/v2/crash-reporting/errors/1400010551/16306/report?pid=1&search=TransactionTooLargeException%20&searchType=detail&bundleId=&channelId=&version=all&tagList=&start=0&date=all)
 *
 *
 * @author yangsk
 * @date 2018/1/10
 */

object NavigationHelper {

    private val sEntry = ConcurrentHashMap<String, Any>()
    var currentPage: Int = 0

    fun put(key: String, value: Any?) {
        if (!TextUtils.isEmpty(key) && null != value) {
            sEntry[key] = value
        } else {
            Logger.e("params is invalid !!!")
        }
    }

     fun get(key: String, remove: Boolean = true): Any? {
        return if (!TextUtils.isEmpty(key)) {
            if (remove) sEntry.remove(key) else sEntry[key]
        } else null
    }



}
