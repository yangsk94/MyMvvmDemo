package com.utils

import android.util.Base64

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.util.Random

/**
 * Created by ysk on 2017/9/13.
 */

object SignUtil {
    val strs = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val key = "7mVkrRMExxTxRVbp"

    val nonce: String
        get() {
            val sb = StringBuffer()
            val chrs = strs.toCharArray()
            for (i in 0..15) {
                val random = Random()
                val index = random.nextInt(chrs.size)
                sb.append(chrs[index])
            }
            return sb.toString()
        }


    @Throws(Exception::class)
    fun encryptHMAC(data: String): String {

        val secretKey = SecretKeySpec(key.toByteArray(), "HmacMD5")//直接用给定的字符串进行
        val mac = Mac.getInstance(secretKey.algorithm)
        mac.init(secretKey)

        return Base64.encodeToString(mac.doFinal(data.toByteArray()), Base64.DEFAULT)

    }
}
