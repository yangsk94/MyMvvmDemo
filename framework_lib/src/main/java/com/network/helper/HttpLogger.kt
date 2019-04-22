package com.network.helper


import com.utils.Logger
import okhttp3.logging.HttpLoggingInterceptor

class HttpLogger : HttpLoggingInterceptor.Logger {

    private val mMessage = StringBuilder()

    override fun log(mes: String) {
        var message = mes
        if (message.startsWith("--> POST")) {
            mMessage.setLength(0)
        }
        if (message.startsWith("{") && message.endsWith("}") || message.startsWith("[") && message.endsWith("]")) {
            message = Logger.formatJson(message)
        }
        mMessage.append(message + "\n")
        if (message.startsWith("<-- END HTTP")) {
            Logger.e(TAG, mMessage.toString())
        }
    }

    companion object {
        private val TAG = HttpLogger::class.java.simpleName
    }
}