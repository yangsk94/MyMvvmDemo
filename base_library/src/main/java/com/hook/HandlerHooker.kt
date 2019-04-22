package com.hook

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.utils.Logger
import com.utils.reflect.FieldUtils

/**
 * Hook系统Handler，防止在部分手机上边抛一些莫名其妙的crash<br></br>
 * 详见：[https://bugly.qq.com/v2/crash-reporting/crashes/1400010551/12519?pid=1](https://bugly.qq.com/v2/crash-reporting/crashes/1400010551/12519?pid=1)
 * <br></br><br></br>
 *
 * @author yangsk
 * @date 2017/11/3
 */

object HandlerHooker {

    private var handler: Handler? = null

    @JvmOverloads
    fun hookSystemHandler(errorCallback: ErrorCallback? = null) {
        try {
            val clazz = Class.forName("android.app.ActivityThread")
            val sCurrentActivityThread = FieldUtils.getDeclaredField(clazz, "sCurrentActivityThread", true)
            if (null != sCurrentActivityThread) {
                val activityThread = sCurrentActivityThread.get(clazz)
                if (null != activityThread) {
                    val mH = FieldUtils.getDeclaredField(clazz, "mH", true)
                    if (null != mH) {
                        val handlerObject = mH.get(activityThread)
                        if (handlerObject is Handler) {
                            val handlerClass = handlerObject.javaClass.superclass
                            val mCallback = FieldUtils.getDeclaredField(handlerClass, "mCallback", true)
                            val mCallbackHandler = FieldUtils.readField(mCallback, handlerObject)
                            val callback: Handler.Callback
                            if (mCallbackHandler is Handler.Callback) {
                                callback = HandlerCallback(handlerObject, mCallbackHandler, errorCallback)
                            } else {
                                callback = HandlerCallback(handlerObject, null, errorCallback)
                            }
                            FieldUtils.writeField(mCallback, handlerObject, callback)
                        }
                    }
                }
            }
        } catch (ignore: Throwable) {
        }

        post(0, Runnable {
            while (true) {
                try {
                    Logger.e("Inner Looper", "Inner Looper loop start ...")
                    Looper.loop()
                } catch (ignored: Throwable) {

                }

                Logger.e("Inner Looper", "Inner Looper loop end ...")
            }
        })
    }

    @Throws(Throwable::class)
    @JvmOverloads
    fun hookToastHandler(toast: Toast?, errorCallback: ErrorCallback? = null) {
        if (null == toast) return
        val mTNObject = FieldUtils.readField(toast, "mTN", true)
        val mHandlerObject = mTNObject?.let { FieldUtils.readField(it, "mHandler", true) }
        if (mHandlerObject is Handler) {
            val mCallbackObject = FieldUtils.readField(mHandlerObject, "mCallback", true)
            val callback: Handler.Callback
            if (mCallbackObject is Handler.Callback) {
                callback = HandlerCallback(mHandlerObject, mCallbackObject, errorCallback)
            } else {
                callback = HandlerCallback(mHandlerObject, null, errorCallback)
            }
            FieldUtils.writeField(mHandlerObject, "mCallback", callback)
        }
    }




    fun post(delay: Int, runnable: Runnable) {
        if (null == handler) {
            handler = Handler(Looper.getMainLooper())
        }
        handler?.postDelayed(runnable, delay.toLong())
    }

    interface ErrorCallback {
        fun error(error: Throwable)
    }
}
