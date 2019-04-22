package com.widget.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;


/**
 * Created by ysk on 2017/4/9.
 */
public class LoadingDrawable extends Drawable implements Animatable
{
    private final BaseLoadingBuilder mBaseLoadingBuilder;

    LoadingDrawable(BaseLoadingBuilder builder)
    {
        this.mBaseLoadingBuilder = builder;
        this.mBaseLoadingBuilder.setCallback(new Callback()
        {
            @Override
            public void invalidateDrawable(Drawable d)
            {
                invalidateSelf();
            }

            @Override
            public void scheduleDrawable(Drawable d, Runnable what, long when)
            {
                scheduleSelf(what, when);
            }

            @Override
            public void unscheduleDrawable(Drawable d, Runnable what)
            {
                unscheduleSelf(what);
            }
        });
    }

    void initParams(Context context)
    {
        if (mBaseLoadingBuilder != null)
        {
            mBaseLoadingBuilder.init(context);
            mBaseLoadingBuilder.initParams(context);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas)
    {
        if (!getBounds().isEmpty())
        {
            this.mBaseLoadingBuilder.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha)
    {
        this.mBaseLoadingBuilder.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter)
    {
        this.mBaseLoadingBuilder.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void start()
    {
        this.mBaseLoadingBuilder.start();
    }

    @Override
    public void stop()
    {
        this.mBaseLoadingBuilder.stop();
    }

    @Override
    public boolean isRunning()
    {
        return this.mBaseLoadingBuilder.isRunning();
    }

    @Override
    public int getIntrinsicHeight()
    {
        return (int) this.mBaseLoadingBuilder.getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth()
    {
        return (int) this.mBaseLoadingBuilder.getIntrinsicWidth();
    }
}
