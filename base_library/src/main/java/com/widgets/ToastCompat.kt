package com.widgets

import android.content.Context
import android.os.Build
import android.support.annotation.StringRes
import android.text.TextUtils
import android.widget.Toast
import com.hook.HandlerHooker
import com.utils.Logger
import com.wram.base_library.R


/**
 * Toast的适配类，防止出现WindowManager$BadTokenException异常
 * <br></br><br></br>
 *
 * @author yangsk
 * @date 2017/6/5
 */

class ToastCompat constructor(
    private val mContext: Context,
    private var mText: String?,
    private val mDuration: Int
) {

    @Synchronized
    fun show() {
        if (TextUtils.isEmpty(mText) || TextUtils.isDigitsOnly(mText) || (mText?.startsWith("-")!!)) {
            Logger.e(javaClass.simpleName, "text invalid !!!")
            mText = mContext.resources.getString(R.string.error_null_text)
            // TODO: 30/09/2018 要求网络异常提示吐司
        }
        val toast = Toast.makeText(mContext, mText, mDuration)
        try {
            if (25 == Build.VERSION.SDK_INT) {
                HandlerHooker.hookToastHandler(toast, object : HandlerHooker.ErrorCallback {
                    override fun error(error: Throwable) {
                        //                        showCustomToast();
                    }
                })
            }
        } catch (throwable: Throwable) {
            // ignore it
        }

        toast.show()
    }

    companion object {

        fun makeText(context: Context, @StringRes stringId: Int, duration: Int = Toast.LENGTH_SHORT): ToastCompat {
            return makeText(context, context.resources.getString(stringId), duration)
        }

        fun makeText(context: Context, text: String, duration: Int = Toast.LENGTH_SHORT): ToastCompat {
            return ToastCompat(context, text, duration)
        }

        fun showToast(context: Context, text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
            ToastCompat.makeText(context, text.toString(), duration).show()
        }

        fun showToast(context: Context, text: CharSequence) {
            ToastCompat.makeText(context, text.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
