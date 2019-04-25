package com.widgets.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 刷新布局
 * Created by yangsk on 2017/4/1.
 */

public class RefreshLayout extends PtrFrameLayout {

    private PtrLayout mHeaderView;

    public RefreshLayout(Context context) {
        super(context);
        initView();
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        mHeaderView = new PtrLayout(getContext());
        setHeaderView(mHeaderView);
        addPtrUIHandler(mHeaderView);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (mKeepClickListener != null) {
            mKeepClickListener.keepClick(e);
        }
        return super.dispatchTouchEvent(e);
    }

    private OnKeepClickListener mKeepClickListener;

    public void setKeepClickListener(OnKeepClickListener mKeepClickListener) {
        this.mKeepClickListener = mKeepClickListener;
    }

    public interface OnKeepClickListener {
        void keepClick(MotionEvent e);
    }
}
