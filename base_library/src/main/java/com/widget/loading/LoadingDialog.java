package com.widget.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wram.base_library.R;

import java.lang.ref.WeakReference;

/**
 * Created by ysk on 2017/4/9.
 */
public class LoadingDialog {
    private final WeakReference<Context> mContext;
    private final int mThemeResId;
    private LoadingType mLoadingBuilderType;
    private int mLoadingBuilderColor;
    private String mHintText;
    private float mHintTextSize = -1;
    private int mHintTextColor = -1;
    private boolean mCancelable;
    private boolean mCanceledOnTouchOutside;
    private double mDurationTimePercent = 1.0f;
    private int mDialogBackgroundColor = -1;
    private Dialog mZLoadingDialog;

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.alert_dialog);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        this.mContext = new WeakReference<>(context);
        this.mThemeResId = themeResId;
    }

    public LoadingDialog setLoadingBuilder(@NonNull LoadingType type) {
        this.mLoadingBuilderType = type;
        return this;
    }

    public LoadingDialog setLoadingColor(@ColorInt int color) {
        this.mLoadingBuilderColor = color;
        return this;
    }

    public LoadingDialog setHintText(String text) {
        this.mHintText = text;
        return this;
    }

    /**
     * 设置了大小后，字就不会有动画了。
     *
     * @param size 大小
     * @return
     */
    public LoadingDialog setHintTextSize(float size) {
        this.mHintTextSize = size;
        return this;
    }

    public LoadingDialog setHintTextColor(@ColorInt int color) {
        this.mHintTextColor = color;
        return this;
    }

    public LoadingDialog setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return this;
    }

    public LoadingDialog setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mCanceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public LoadingDialog setDurationTime(double percent) {
        this.mDurationTimePercent = percent;
        return this;
    }

    public LoadingDialog setDialogBackgroundColor(@ColorInt int color) {
        this.mDialogBackgroundColor = color;
        return this;
    }

    private
    @NonNull
    View createContentView() {
        if (isContextNotExist()) {
            throw new RuntimeException("Context is null...");
        }
        return View.inflate(this.mContext.get(), R.layout.dialog_loading, null);
    }

    public Dialog create() {
        if (isContextNotExist()) {
            throw new RuntimeException("Context is null...");
        }
        if (mZLoadingDialog != null) {
            cancel();
        }
        mZLoadingDialog = new Dialog(this.mContext.get(), this.mThemeResId);
        View contentView = createContentView();
        LinearLayout zLoadingRootView = contentView.findViewById(R.id.z_loading);

        // init color
        if (this.mDialogBackgroundColor != -1) {
            final Drawable drawable = zLoadingRootView.getBackground();
            if (drawable != null) {
                drawable.setAlpha(Color.alpha(this.mDialogBackgroundColor));
                drawable.setColorFilter(this.mDialogBackgroundColor, PorterDuff.Mode.SRC_ATOP);
            }
        }

        LoadingView loadingView = contentView.findViewById(R.id.z_loading_view);
        LoadingTextView zTextView = contentView.findViewById(R.id.z_text_view);
        TextView zCustomTextView = contentView.findViewById(R.id.z_custom_text_view);
        if (this.mHintTextSize > 0 && !TextUtils.isEmpty(mHintText)) {
            zCustomTextView.setVisibility(View.VISIBLE);
            zCustomTextView.setText(mHintText);
            zCustomTextView.setTextSize(this.mHintTextSize);
            zCustomTextView.setTextColor(this.mHintTextColor == -1 ? this.mLoadingBuilderColor : this.mHintTextColor);
        } else if (!TextUtils.isEmpty(mHintText)) {
            zTextView.setVisibility(View.VISIBLE);
            zTextView.setText(mHintText);
            zTextView.setColorFilter(this.mHintTextColor == -1 ? this.mLoadingBuilderColor : this.mHintTextColor, PorterDuff.Mode.SRC_ATOP);
        }
        loadingView.setLoadingBuilder(this.mLoadingBuilderType);
        // 设置间隔百分比
        if (loadingView.mBaseLoadingBuilder != null) {
            loadingView.mBaseLoadingBuilder.setDurationTimePercent(this.mDurationTimePercent);
        }
        loadingView.setColorFilter(this.mLoadingBuilderColor, PorterDuff.Mode.SRC_ATOP);
        mZLoadingDialog.setContentView(contentView);
        mZLoadingDialog.setCancelable(mCancelable);
        mZLoadingDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        return mZLoadingDialog;
    }

    public void show() {
        if (mZLoadingDialog != null) {
            mZLoadingDialog.show();
        } else {
            final Dialog zLoadingDialog = create();
            zLoadingDialog.show();
        }
    }

    public void cancel() {
        if (mZLoadingDialog != null) {
            mZLoadingDialog.cancel();
        }
        mZLoadingDialog = null;
    }

    public void dismiss() {
        if (mZLoadingDialog != null) {
            mZLoadingDialog.dismiss();
        }
        mZLoadingDialog = null;
    }

    public boolean isShowing() {
        if (mZLoadingDialog == null) {
            return false;
        }
        return mZLoadingDialog.isShowing();
    }

    public boolean isGetWindow() {
        if (mZLoadingDialog == null) {
            return false;
        }
        return null != mZLoadingDialog.getWindow();
    }

    public boolean isAttachedToWindow() {
        if (mZLoadingDialog == null) {
            return false;
        }
        return ViewCompat.isAttachedToWindow(mZLoadingDialog.getWindow().getDecorView());
    }

    private boolean isContextNotExist() {
        Context context = this.mContext.get();
        return context == null;
    }
}
