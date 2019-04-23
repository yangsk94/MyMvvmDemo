package com.common.base;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.utils.ViewUtil;

/**
 * 组合View的基类
 * Created by yangsk
 */

public abstract class BaseConstraintLayout<B extends ViewDataBinding> extends ConstraintLayout {

    protected B mBinding;

    public BaseConstraintLayout(Context context) {
        super(context);
        setLayoutOrientation();
        int resId = getLayoutResId();
        if (ViewUtil.Companion.isResourceIdValid(resId)) {

            View view = LayoutInflater.from(context).inflate(resId, this);
            view.setTag("layout/" + getLayoutResIdName() + "_0");

            mBinding = DataBindingUtil.bind(view);

        }
        initView();

    }

    public BaseConstraintLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutOrientation();
        int resId = getLayoutResId();
        if (ViewUtil.Companion.isResourceIdValid(resId)) {

            View view = LayoutInflater.from(context).inflate(resId, this);
            view.setTag("layout/" + getLayoutResIdName() + "_0");

            mBinding = DataBindingUtil.bind(view);

        }


        readXmlAttribute(attrs);
        initView();
    }

    public BaseConstraintLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutOrientation();
        int resId = getLayoutResId();
        if (ViewUtil.Companion.isResourceIdValid(resId)) {

            View view = LayoutInflater.from(context).inflate(resId, this);
            view.setTag("layout/" + getLayoutResIdName() + "_0");

            mBinding = DataBindingUtil.bind(view);

        }
        readXmlAttribute(attrs);
        initView();
    }

    protected void setLayoutOrientation() {

    }


    protected void readXmlAttribute(AttributeSet attrs) {

    }

    @Override
    protected void onDetachedFromWindow() {
        if (needUnbindOps()) {
            destroyResource();
        }
        super.onDetachedFromWindow();
    }

    protected void destroyResource() {
        //release your view
    }

    protected void destroyResourceInner(ImageView imageView) {
        if (null != imageView) {
            ViewUtil.Companion.unbindDrawable(imageView);
            imageView.setOnClickListener(null);
        }
    }

    protected boolean needUnbindOps() {
        return true;
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    @NonNull
    protected abstract String getLayoutResIdName();

    protected abstract void initView();
}
