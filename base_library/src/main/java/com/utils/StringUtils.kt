package com.utils

/**
 * @author yangsk
 * @class describe  [.]
 * @time 2019/4/18 下午1:13
 */
object StringUtils {


    fun formatBuilder(vararg strings: String): String {
        val buffer = StringBuilder()
        for (string in strings) {
            buffer.append(string)
        }
        return buffer.toString()
    }


    fun formatBuffer(vararg strings: String): String {
        val buffer = StringBuffer()
        for (string in strings) {
            buffer.append(string)
        }
        return buffer.toString()
    }


}
