package com.utils

import android.util.Log

/**
 * Created by yangsk on 2017/6/19.
 */

object Logger {

    var DEBUG: Boolean = false

    private val TAG = Logger::class.java.simpleName

    private val LOG_MAX_LENGTH = 2000

    fun debugEnabled(enabled: Boolean) {
        DEBUG = enabled
    }

    fun i(msg: String) {
        i(TAG, msg)
    }

    fun i(tag: String, msg: String?) {
        if (DEBUG && null != msg) {
            Log.i(tag, msg)
        }
    }

    fun d(msg: String) {
        d(TAG, msg)
    }

    fun d(tag: String, msg: String?) {
        if (DEBUG && null != msg) {
            Log.d(tag, msg)
        }
    }

    fun v(msg: String) {
        v(TAG, msg)
    }

    fun v(tag: String, msg: String?) {
        if (DEBUG && null != msg) {
            Log.v(tag, msg)
        }
    }

    fun w(msg: String) {
        w(TAG, msg)
    }

    fun w(tag: String, msg: String?) {
        if (DEBUG && null != msg) {
            Log.w(tag, msg)
        }
    }

    fun e(msg: String) {
        e(TAG, msg)
    }


    fun e(e: Throwable?) {
        if (DEBUG && null != e) {
            e.printStackTrace()
        }
    }

    fun i(msg: String, split: Boolean) {
        if (!split) {
            i(TAG, msg)
        } else {
            for (i in 0..msg.length / LOG_MAX_LENGTH) {
                if (i * LOG_MAX_LENGTH + LOG_MAX_LENGTH >= msg.length) {
                    val msg_1 = msg.substring(i * LOG_MAX_LENGTH, msg.length)
                    i(TAG, msg_1)
                } else {
                    val msg_1 = msg.substring(i * LOG_MAX_LENGTH, i * LOG_MAX_LENGTH + LOG_MAX_LENGTH)
                    i(TAG, msg_1)
                }
            }
        }
    }

    fun e(tag: String, msg: String) {
        if (DEBUG) {
            var msg = msg
            val max_str_length = 2001 - tag.length
            while (msg.length > max_str_length) {
                Log.e(tag, msg.substring(0, max_str_length))
                msg = msg.substring(max_str_length)
            }
            Log.e(tag, msg)
        }
    }

    /**
     * 格式化json字符串
     *
     * @param jsonStr 需要格式化的json串
     * @return 格式化后的json串
     */
    fun formatJson(jsonStr: String?): String {
        if (null == jsonStr || "" == jsonStr) {
            return ""
        }
        val sb = StringBuilder()
        var last = '\u0000'
        var current = '\u0000'
        var indent = 0
        for (i in 0 until jsonStr.length) {
            last = current
            current = jsonStr[i]
            //遇到{ [换行，且下一行缩进
            when (current) {
                '{', '[' -> {
                    sb.append(current)
                    sb.append('\n')
                    indent++
                    addIndentBlank(sb, indent)
                }
                //遇到} ]换行，当前行缩进
                '}', ']' -> {
                    sb.append('\n')
                    indent--
                    addIndentBlank(sb, indent)
                    sb.append(current)
                }
                //遇到,换行
                ',' -> {
                    sb.append(current)
                    if (last != '\\') {
                        sb.append('\n')
                        addIndentBlank(sb, indent)
                    }
                }
                else -> sb.append(current)
            }
        }
        return sb.toString()
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private fun addIndentBlank(sb: StringBuilder, indent: Int) {
        for (i in 0 until indent) {
            sb.append('\t')
        }
    }

    /**
     * http 请求数据返回 json 中中文字符为 unicode 编码转汉字转码
     *
     * @param theString
     * @return 转化后的结果.
     */
    fun decodeUnicode(theString: String): String {
        var aChar: Char
        val len = theString.length
        val outBuffer = StringBuffer(len)
        var x = 0
        while (x < len) {
            aChar = theString[x++]
            if (aChar == '\\') {
                aChar = theString[x++]
                if (aChar == 'u') {
                    var value = 0
                    for (i in 0..3) {
                        aChar = theString[x++]
                        when (aChar) {
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> value =
                                (value shl 4) + aChar.toInt() - '0'.toInt()
                            'a', 'b', 'c', 'd', 'e', 'f' -> value = (value shl 4) + 10 + aChar.toInt() - 'a'.toInt()
                            'A', 'B', 'C', 'D', 'E', 'F' -> value = (value shl 4) + 10 + aChar.toInt() - 'A'.toInt()
                            else -> throw IllegalArgumentException(
                                "Malformed   \\uxxxx   encoding."
                            )
                        }

                    }
                    outBuffer.append(value.toChar())
                } else {
                    if (aChar == 't') {
                        aChar = '\t'
                    } else if (aChar == 'r') {
                        aChar = '\r'
                    } else if (aChar == 'n') {
                        aChar = '\n'
                    }
                    outBuffer.append(aChar)
                }
            } else {
                outBuffer.append(aChar)
            }
        }
        return outBuffer.toString()
    }

}
