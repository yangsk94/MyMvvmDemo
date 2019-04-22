package com.hook

import android.os.Handler
import android.os.Message
import com.utils.Logger

/**
 * 系统回调方法，在这里添加Hook操作，防止程序抛异常
 * <br></br><br></br>
 *
 * @author yangsk
 * @date 2017/11/3
 */
 class HandlerCallback  constructor(
    private val mTargetHandler: Handler?,
    private val mTargetCallback: Handler.Callback?,
    private val mErrorCallback: HandlerHooker.ErrorCallback?
) : Handler.Callback {

    override fun handleMessage(msg: Message): Boolean {
        try {
            if (null != mTargetCallback) {
                return mTargetCallback.handleMessage(msg)
            } else if (null != mTargetHandler) {
                mTargetHandler.handleMessage(msg)
            }
        } catch (throwable: Throwable) {
            if (null != mErrorCallback) {
                mErrorCallback.error(throwable)
            } else {
                Logger.e(throwable.toString())
            }
        }

        return true
    }
}