package com.widget.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.wram.base_library.R;

import java.util.Observable;
import java.util.Observer;


/**
 * Created by ysk on 2017/4/9. https://github.com/zyao89/ZLoading
 */
public class LoadingView extends ImageView  {
    private LoadingDrawable mLoadingDrawable;
    protected BaseLoadingBuilder mBaseLoadingBuilder;
//    private TimeEventEmitter mEmitter;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        try {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
            int typeId = ta.getInt(R.styleable.LoadingView_z_type, 0);
            int color = ta.getColor(R.styleable.LoadingView_z_color, Color.BLACK);
            float durationTimePercent = ta.getFloat(R.styleable.LoadingView_z_duration_percent, 1.0f);
            ta.recycle();
            setLoadingBuilder(LoadingType.values()[typeId], durationTimePercent);
            setColorFilter(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLoadingBuilder(@NonNull LoadingType builder) {
        mBaseLoadingBuilder = builder.newInstance();
        initZLoadingDrawable();
    }

    public void setLoadingBuilder(@NonNull LoadingType builder, double durationPercent) {
        this.setLoadingBuilder(builder);
        initDurationTimePercent(durationPercent);
    }

    private void initZLoadingDrawable() {
        if (mBaseLoadingBuilder == null) {
            throw new RuntimeException("mBaseLoadingBuilder is null.");
        }
        mLoadingDrawable = new LoadingDrawable(mBaseLoadingBuilder);
        mLoadingDrawable.initParams(getContext());
        setImageDrawable(mLoadingDrawable);
    }

    private void initDurationTimePercent(double durationPercent) {
        if (mBaseLoadingBuilder == null) {
            throw new RuntimeException("mBaseLoadingBuilder is null.");
        }
        mBaseLoadingBuilder.setDurationTimePercent(durationPercent);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();

    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        final boolean visible = visibility == VISIBLE && getVisibility() == VISIBLE;
        if (visible) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    private void startAnimation() {
        if (mLoadingDrawable != null) {
            mLoadingDrawable.start();
        }
    }

    private void stopAnimation() {
        if (mLoadingDrawable != null) {
            mLoadingDrawable.stop();
        }
    }
}
