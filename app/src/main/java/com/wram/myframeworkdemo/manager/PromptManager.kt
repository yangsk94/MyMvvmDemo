package com.wram.myframeworkdemo.manager

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.support.v4.view.ViewCompat
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.navigation.BaseActivity
import com.utils.Logger
import com.utils.TextStyleUtil
import com.utils.ViewUtil
import com.widget.loading.LoadingDialog
import com.widget.loading.LoadingType
import com.wram.base_library.R
import java.util.*

/**
 * Created by yangsk on 2017/7/3.
 * <br></br><br></br>
 * 全局Dialog管理类
 */

class PromptManager private constructor() {

    private val mDialogs: HashMap<String, Dialog>?

    private val mLoadDialogs: HashMap<String, LoadingDialog>?

    fun showLoading(context: Context) {
        val dialog = LoadingDialog(context)
        mLoadDialogs?.set(context.javaClass.simpleName, dialog)
        dialog.setLoadingBuilder(LoadingType.ROTATE_CIRCLE)
            .setLoadingColor(Color.parseColor("#1A2027"))
            //                .setHintText("Loading...")
            //                .setHintTextColor(Color.GRAY)  // 设置字体颜色
            //                                .setHintTextSize(16) // 设置字体大小
            //                                .setDurationTime(0.5) // 设置动画时间百分比
            .setDialogBackgroundColor(Color.parseColor("#cc111111")) // 设置背景色
            .show()
    }

    init {
        mDialogs = HashMap()
        mLoadDialogs = HashMap()
    }

    fun showProgressDialog(context: Context, percent: Int) {
        showProgressDialog(context, "请等候...($percent%)", false)
    }

    fun showProgressDialog(context: Context, percent: Double) {
        showProgressDialog(context, "请等候...($percent%)", false)
    }

    fun showProgressDialog(context: Context, cancelable: Boolean) {
        showProgressDialog(context, null, cancelable)
    }

    @JvmOverloads
    fun showProgressDialog(context: Context, content: String? = null, cancelable: Boolean = false) {
        if (context !is Activity) {
            Logger.e(PromptManager::class.java.simpleName, "context must be instance of Activity !!!")
            return
        }

        if (context.isFinishing) {
            Logger.e(PromptManager::class.java.simpleName, "activity is finishing !!!")
            return
        }

        val dialog = ProgressDialog(context)
        dialog.setCanceledOnTouchOutside(cancelable)
        dialog.setCancelable(cancelable)

        if (!TextUtils.isEmpty(content)) {
            dialog.setMessage(content)
        }

        mDialogs?.set(context.javaClass.simpleName, dialog)
        dialog.show()
    }

    fun dismissProgressDialog(context: Context?) {
        if (null == context || null == mDialogs || null == mLoadDialogs) {
            return
        }
        val dialog = mDialogs.remove(context.javaClass.simpleName)
        dismissDialogInternal(dialog)
    }

    fun dismissLoadingDialog(context: Context?) {
        if (null == context || null == mLoadDialogs) {
            return
        }
        val dialog = mLoadDialogs.remove(context.javaClass.simpleName)
        dismissDialogInternal(dialog)
    }

    fun showCommonDialog(
        context: Context,
        title: String,
        msg: String,
        ok: String,
        no: String,
        callback: CommonDialogCallback
    ) {
        showCommonDialog(context, title, msg, ok, no, false, callback)
    }

    fun showCommonDialog(
        context: Context,
        title: String,
        msg: String,
        ok: String,
        no: String,
        cancelable: Boolean,
        callback: CommonDialogCallback?
    ) {
        if (context !is BaseActivity) {
            return
        }

        if (!context.isOnResume || context.isDestroyed || context.isFinishing) {
            return
        }
        val view = View.inflate(context, R.layout.common_dailog_layout, null)
        val dialog = Dialog(context, R.style.dialog)
        dialog.setCanceledOnTouchOutside(cancelable)
        dialog.setCancelable(cancelable)

        val message = view.findViewById<TextView>(R.id.common_dialog_content_view)
        message.text = msg

        val mTitle = view.findViewById<TextView>(R.id.common_dialog_title_view)
        if (TextUtils.isEmpty(title)) {
            ViewUtil.updateViewVisibility(mTitle, false)
            ViewUtil.updateViewVisibility(view.findViewById(R.id.common_dialog_title_line), false)
        } else {
            mTitle.text = title
            TextStyleUtil.setFakeBold(message, false)
            message.textSize = 13f
            message.setTextColor(context.getResources().getColor(R.color.common_title_bar))
        }

        var line = 0
        if (!TextUtils.isEmpty(ok)) {
            val button = view.findViewById<TextView>(R.id.common_dialog_ok_button)
            ViewUtil.updateViewVisibility(button, true)
            button.text = ok
            button.setOnClickListener {
                dismissDialogInternal(dialog)
                callback?.onOkClick()
            }
            line++
        }

        if (!TextUtils.isEmpty(no)) {
            val button = view.findViewById<TextView>(R.id.common_dialog_no_button)
            ViewUtil.updateViewVisibility(button, true)
            button.text = no
            button.setOnClickListener {
                dismissDialogInternal(dialog)
                callback?.onNoClick()
            }
            line++
        }

        if (2 == line) {
            ViewUtil.updateViewVisibility(view.findViewById(R.id.common_dialog_separate_line), true)
        }
        dialog.setContentView(view)
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                dialog.show()
            } catch (ignore: Throwable) {
            }
        }, 150)

    }

    private fun dismissDialogInternal(dialog: Dialog?) {
        var dialog = dialog
        if (null != dialog && dialog.isShowing
            && null != dialog.window
            && ViewCompat.isAttachedToWindow(dialog.window.decorView)
        ) {
            dialog.dismiss()
        }
        dialog = null
    }

    private fun dismissDialogInternal(dialog: LoadingDialog?) {
        var dialog = dialog
        if (null != dialog && dialog.isShowing && dialog.isGetWindow && dialog.isAttachedToWindow) {
            dialog.dismiss()
        }
        dialog = null
    }

    class SimpleDialogCallback : CommonDialogCallback {
        override fun onOkClick() {}

        override fun onNoClick() {

        }
    }

    interface CommonDialogCallback {

        fun onOkClick()

        fun onNoClick()
    }

    companion object {

        private var INSTANCE: PromptManager? = null

        val impl: PromptManager?
            get() {
                if (null == INSTANCE) {
                    synchronized(PromptManager::class.java) {
                        if (null == INSTANCE) {
                            INSTANCE = PromptManager()
                        }
                    }
                }
                return INSTANCE
            }
    }
}
