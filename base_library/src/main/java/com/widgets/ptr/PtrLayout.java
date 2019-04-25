package com.widgets.ptr;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import com.common.base.BaseFrameLayout;
import com.wram.base_library.R;
import com.wram.base_library.databinding.ViewRefreshHeaderBinding;

/**
 * 刷新头布局
 * <p>
 * * Created by yangsk on 2017/4/1.
 */

public class PtrLayout extends BaseFrameLayout<ViewRefreshHeaderBinding> implements PtrUIHandler {

    private static final float MARGIN_RIGHT = 40;

    /**
     * 状态识别
     */
    private int mState;


    /**
     * 重置
     * 准备刷新
     * 开始刷新
     * 结束刷新
     */
    public static final int STATE_RESET = -1;
    public static final int STATE_PREPARE = 0;
    public static final int STATE_BEGIN = 1;
    public static final int STATE_FINISH = 2;
    private Animation mAnimation;

    public PtrLayout(@NonNull Context context) {
        super(context);
    }

    public PtrLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_refresh_header;
    }

    @NonNull
    @Override
    protected String getLayoutResIdName() {
        return "view_refresh_header";
    }

    @Override
    protected void initView() {

    }


    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mState = STATE_RESET;
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mState = STATE_PREPARE;
        mBinding.ivRefreshIcon.setAlpha(1.0f);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mState = STATE_BEGIN;
        //隐藏logo,开启跑步动画
        mBinding.tvRefresh.setTextColor(getResources().getColor(R.color.c_1a2027));
        try {
            if (null == mAnimation) {
                mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_ptr_icon);
            }
        } catch (Resources.NotFoundException e) {
            initAnimationDynamic();
        } finally {
            mBinding.ivRefreshIcon.startAnimation(mAnimation);
        }
    }

    private void initAnimationDynamic() {
        if (null != mAnimation) {
            return;
        }
        mAnimation = new RotateAnimation(0, 3600, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        mAnimation.setDuration(15000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setFillAfter(true);
        mAnimation.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mState = STATE_FINISH;
        mBinding.ivRefreshIcon.clearAnimation();
        if (mAnimation != null) {
            mAnimation.reset();
        }
        mBinding.tvRefresh.setTextColor(getResources().getColor(R.color.c_1a2027));
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        //处理提醒字体
        switch (mState) {
            case STATE_PREPARE:
//                Logger.i("roomMsg", "----------->refresh=" + ptrIndicator.getCurrentPercent());
                //logo设置
                mBinding.ivRefreshIcon.setAlpha(ptrIndicator.getCurrentPercent());
                LinearLayout.LayoutParams mIvManLayoutParams = (LinearLayout.LayoutParams) mBinding.ivRefreshIcon.getLayoutParams();
                if (ptrIndicator.getCurrentPercent() <= 1) {
                    int marginRight = (int) (MARGIN_RIGHT - MARGIN_RIGHT * ptrIndicator.getCurrentPercent() / 2);
                    mIvManLayoutParams.setMargins(0, 0, marginRight, 0);
                    mBinding.ivRefreshIcon.setLayoutParams(mIvManLayoutParams);
                }
                mBinding.ivRefreshIcon.setRotation(ptrIndicator.getCurrentPercent() * 360);
                if (ptrIndicator.getCurrentPercent() < 1.2) {
                    mBinding.tvRefresh.setText(R.string.ptr_continue_pull);
                } else {
                    mBinding.tvRefresh.setText(R.string.ptr_let_it_go);
                }
                mBinding.tvRefresh.setTextColor(getResources().getColor(R.color.c_1a2027));
                break;
            case STATE_BEGIN:
                mBinding.tvRefresh.setText(R.string.ptr_refreshing);
                break;
            case STATE_FINISH:
                mBinding.tvRefresh.setText(R.string.ptr_refresh_complete);
                break;
        }
    }

}
