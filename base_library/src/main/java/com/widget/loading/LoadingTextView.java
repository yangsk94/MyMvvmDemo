package com.widget.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.wram.base_library.R;


/**
 * Created by ysk on 2017/4/9.
 */
public class LoadingTextView extends LoadingView
{
    private String mText = "Zyao89";

    public LoadingTextView(Context context)
    {
        this(context, null);
    }

    public LoadingTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, -1);
    }

    public LoadingTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    @Deprecated
    public void setLoadingBuilder(@NonNull LoadingType builder)
    {
        super.setLoadingBuilder(LoadingType.TEXT);
    }

    public void setText(String text)
    {
        this.mText = text;
        if (mBaseLoadingBuilder instanceof TextBuilder)
        {
            ((TextBuilder) mBaseLoadingBuilder).setText(mText);
        }
    }

    private void init(Context context, AttributeSet attrs)
    {
        super.setLoadingBuilder(LoadingType.TEXT);
        try
        {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingTextView);
            String text = ta.getString(R.styleable.LoadingTextView_z_text);
            ta.recycle();
            if (!TextUtils.isEmpty(text))
            {
                this.mText = text;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onAttachedToWindow()
    {
        setText(mText);
        super.onAttachedToWindow();
    }
}
