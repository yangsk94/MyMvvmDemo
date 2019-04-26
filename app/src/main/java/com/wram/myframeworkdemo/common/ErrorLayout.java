package com.wram.myframeworkdemo.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.common.base.BaseFrameLayout;
import com.utils.ViewUtil;
import com.wram.myframeworkdemo.BR;
import com.wram.myframeworkdemo.R;
import com.wram.myframeworkdemo.databinding.ErrorLayoutBinding;
import com.wram.myframeworkdemo.me.MeVM;

/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/26 上午10:50
 */
public class ErrorLayout extends BaseFrameLayout<ErrorLayoutBinding> implements View.OnClickListener {
    private String mHint;
    private boolean isShowRefresh;
    private boolean isShowLoading;

    public void setViewModel(MeVM mViewModel) {
        mBinding.setVariable(BR.mvm, mViewModel);
        mBinding.executePendingBindings();
    }

    public ErrorLayout(Context context) {
        super(context);
    }

    public ErrorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.error_layout;
    }


    @Override
    public void readXmlAttribute(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ErrorView);
        mHint = TextUtils.isEmpty(array.getString(R.styleable.ErrorView_hint))
                ? getContext().getResources().getString(R.string.common_error_text)
                : array.getString(R.styleable.ErrorView_hint);
        isShowRefresh = array.getBoolean(R.styleable.ErrorView_isShowRefresh, false);
        isShowLoading = array.getBoolean(R.styleable.ErrorView_isShowLoading, false);
        array.recycle();
    }

    @NonNull
    @Override
    protected String getLayoutResIdName() {
        return "error_layout";
    }

    @Override
    protected void initView() {

        mBinding.baseRefreshBtn.setOnClickListener(this);
        ViewUtil.Companion.updateViewVisibility(mBinding.baseRefreshParent, isShowRefresh || isShowLoading);
        mBinding.baseRefreshText.setText(mHint);
    }


    public void showError() {
        isShowRefresh = true;
        ViewUtil.Companion.updateViewVisibility(mBinding.baseRefreshParent, true);
        ViewUtil.Companion.updateViewVisibility(mBinding.errorProgressView, false);
        ViewUtil.Companion.updateViewVisibility(mBinding.baseRefresh, true);
    }


    public void showLoading() {
        ViewUtil.Companion.updateViewVisibility(mBinding.baseRefreshParent, true);
        ViewUtil.Companion.updateViewVisibility(mBinding.errorProgressView, true);
        ViewUtil.Companion.updateViewVisibility(mBinding.baseRefresh, false);
    }

    public void updateVisibility(boolean visible) {
        ViewUtil.Companion.updateViewVisibility(this, visible);
    }

    @Override
    public void onClick(View view) {

        showLoading();

    }

    private ClickRefreshListener listener;

    public void setClickRefreshListener(ClickRefreshListener listener) {
        this.listener = listener;
    }

    public interface ClickRefreshListener {
        void onClickRefreshListener();
    }
}
