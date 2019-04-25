package com.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.BaseApplication;


/**
 * 自定义ImageView，对draw()和onDraw()方法进行try-catch操作<br/>
 * 防止出现trying to use a recycled bitmap android.graphics.Bitmap@b90a034的异常引发应用crash<br/>
 * <br/><br/>
 *
 * @author yangsk
 * @date 2017/10/18
 */

public class ImageView extends android.widget.ImageView {

    public ImageView(Context context) {
        super(context);
        disableHardwareBelowLOLLIPOP(context);
    }

    public ImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        disableHardwareBelowLOLLIPOP(context);
    }

    public ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        disableHardwareBelowLOLLIPOP(context);
    }

    @TargetApi(21)
    public ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        disableHardwareBelowLOLLIPOP(context);
    }

    /**
     * 对于5.0以下手机，尝试关闭硬件加速功能，避免出现bugly上的bug<a href='https://bugly.qq.com/v2/crash-reporting/crashes/1400010551/14466?pid=1'>https://bugly.qq.com/v2/crash-reporting/crashes/1400010551/14466?pid=1</a>
     */
    private void disableHardwareBelowLOLLIPOP(Context context) {
        if (Build.VERSION.SDK_INT < 21) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            super.draw(canvas);
        } catch (Throwable e) {
            if (BaseApplication.Companion.getContext().isDebuggable()) {
                throw e;
            } else {
                //上报错误日志
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            if (BaseApplication.Companion.getContext().isDebuggable()) {
                throw e;
            } else {
                //上报错误日志
            }
        }
    }
 }
