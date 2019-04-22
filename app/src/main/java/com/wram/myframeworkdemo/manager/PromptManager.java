package com.wram.myframeworkdemo.manager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.navigation.BaseActivity;
import com.utils.Logger;
import com.utils.TextStyleUtil;
import com.utils.ViewUtil;
import com.widget.loading.LoadingDialog;
import com.widget.loading.LoadingType;
import com.wram.base_library.R;

import java.util.HashMap;

/**
 * Created by yangsk on 2017/7/3.
 * <br/><br/>
 * 全局Dialog管理类
 */

public class PromptManager {

    private static PromptManager INSTANCE;

    private HashMap<String, Dialog> mDialogs;

    private HashMap<String, LoadingDialog> mLoadDialogs;

    public static PromptManager getIMPL() {
        if (null == INSTANCE) {
            synchronized (PromptManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new PromptManager();
                }
            }
        }
        return INSTANCE;
    }

    public void showLoading(Context context) {
        LoadingDialog dialog = new LoadingDialog(context);
        mLoadDialogs.put(context.getClass().getSimpleName(), dialog);
        dialog.setLoadingBuilder(LoadingType.ROTATE_CIRCLE)
                .setLoadingColor(Color.parseColor("#1A2027"))
//                .setHintText("Loading...")
//                .setHintTextColor(Color.GRAY)  // 设置字体颜色
//                                .setHintTextSize(16) // 设置字体大小
//                                .setDurationTime(0.5) // 设置动画时间百分比
                .setDialogBackgroundColor(Color.parseColor("#cc111111")) // 设置背景色
                .show();
    }

    private PromptManager() {
        mDialogs = new HashMap<>();
        mLoadDialogs = new HashMap<>();
    }

    public void showProgressDialog(Context context) {
        showProgressDialog(context, null, false);
    }

    public void showProgressDialog(Context context, int percent) {
        showProgressDialog(context, "请等候...(" + percent + "%)", false);
    }

    public void showProgressDialog(Context context, double percent) {
        showProgressDialog(context, "请等候...(" + percent + "%)", false);
    }

    public void showProgressDialog(Context context, boolean cancelable) {
        showProgressDialog(context, null, cancelable);
    }

    public void showProgressDialog(Context context, String content, boolean cancelable) {
        if (!(context instanceof Activity)) {
            Logger.INSTANCE.e(PromptManager.class.getSimpleName(), "context must be instance of Activity !!!");
            return;
        }

        Activity activity = (Activity) context;
        if (activity.isFinishing()) {
            Logger.INSTANCE.e(PromptManager.class.getSimpleName(), "activity is finishing !!!");
            return;
        }

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.setCancelable(cancelable);

        if (!TextUtils.isEmpty(content)) {
            dialog.setMessage(content);
        }

        mDialogs.put(context.getClass().getSimpleName(), dialog);
        dialog.show();
    }

    public void dismissProgressDialog(Context context) {
        if (null == context || null == mDialogs || null == mLoadDialogs) {
            return;
        }
        final Dialog dialog = mDialogs.remove(context.getClass().getSimpleName());
        dismissDialogInternal(dialog);
    }

    public void dismissLoadingDialog(Context context) {
        if (null == context || null == mLoadDialogs) {
            return;
        }
        LoadingDialog dialog = mLoadDialogs.remove(context.getClass().getSimpleName());
        dismissDialogInternal(dialog);
    }

    public void showCommonDialog(Context context, String title, String msg, String ok, String no, final CommonDialogCallback callback) {
        showCommonDialog(context, title, msg, ok, no, false, callback);
    }

    public void showCommonDialog(final Context context, String title, String msg, String ok, String no, boolean cancelable, final CommonDialogCallback callback) {
        if (!(context instanceof BaseActivity)) {
            return;
        }

        BaseActivity baseActivity = (BaseActivity) context;
        if (!baseActivity.isOnResume() || baseActivity.isDestroyed() || baseActivity.isFinishing()) {
            return;
        }
        View view = View.inflate(context, R.layout.common_dailog_layout, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.setCancelable(cancelable);

        TextView message = view.findViewById(R.id.common_dialog_content_view);
        message.setText(msg);

        TextView mTitle = view.findViewById(R.id.common_dialog_title_view);
        if (TextUtils.isEmpty(title)) {
            ViewUtil.Companion.updateViewVisibility(mTitle, false);
            ViewUtil.Companion.updateViewVisibility(view.findViewById(R.id.common_dialog_title_line), false);
        } else {
            mTitle.setText(title);
            TextStyleUtil.Companion.setFakeBold(message, false);
            message.setTextSize(13);
            message.setTextColor(context.getResources().getColor(R.color.common_title_bar));
        }

        int line = 0;
        if (!TextUtils.isEmpty(ok)) {
            TextView button = view.findViewById(R.id.common_dialog_ok_button);
            ViewUtil.Companion.updateViewVisibility(button, true);
            button.setText(ok);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialogInternal(dialog);
                    if (null != callback) {
                        callback.onOkClick();
                    }
                }
            });
            line++;
        }

        if (!TextUtils.isEmpty(no)) {
            TextView button = view.findViewById(R.id.common_dialog_no_button);
            ViewUtil.Companion.updateViewVisibility(button, true);
            button.setText(no);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialogInternal(dialog);
                    if (null != callback) {
                        callback.onNoClick();
                    }
                }
            });
            line++;
        }

        if (2 == line) {
            ViewUtil.Companion.updateViewVisibility(view.findViewById(R.id.common_dialog_separate_line), true);
        }
        dialog.setContentView(view);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    dialog.show();
                } catch (Throwable ignore) {
                }
            }
        }, 150);

    }

    private void dismissDialogInternal(Dialog dialog) {
        if (null != dialog && dialog.isShowing()
                && null != dialog.getWindow()
                && ViewCompat.isAttachedToWindow(dialog.getWindow().getDecorView())) {
            dialog.dismiss();
        }
        dialog = null;
    }

    private void dismissDialogInternal(LoadingDialog dialog) {
        if (null != dialog && dialog.isShowing() && dialog.isGetWindow() && dialog.isAttachedToWindow()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    public static class SimpleDialogCallback implements CommonDialogCallback {
        @Override
        public void onOkClick() {
        }

        @Override
        public void onNoClick() {

        }
    }

    public interface CommonDialogCallback {

        void onOkClick();

        void onNoClick();
    }
}
