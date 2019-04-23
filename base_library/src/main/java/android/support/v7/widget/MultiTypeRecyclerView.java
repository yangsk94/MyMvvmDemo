package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 尝试修复：Cannot call this method while RecyclerView is computing a layout or scrolling的bug<br/>
 * 详见：<a href='https://bugly.qq.com/v2/crash-reporting/errors/1400010551/22771/report?pid=1&search=XcbbCatchedException&searchType=detail&bundleId=&channelId=&version=1.4.2&tagList=&start=0&date=all'>这里</a>
 * <br/><br/>
 *
 * @author ysk
 * @date 2018/2/11
 */

public class MultiTypeRecyclerView extends RecyclerView implements MultiTypeObservable.LayoutObserver {

    public MultiTypeRecyclerView(Context context) {
        super(context);
    }

    public MultiTypeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiTypeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof MultiTypeAdapter) {
            MultiTypeAdapter multiTypeAdapter = (MultiTypeAdapter) adapter;
            multiTypeAdapter.registerLayoutObserver(this);
        }
        super.setAdapter(adapter);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (getAdapter() instanceof MultiTypeAdapter) {
            MultiTypeAdapter multiTypeAdapter = (MultiTypeAdapter) getAdapter();
            multiTypeAdapter.unregisterLayoutObserver();
        }
        super.onDetachedFromWindow();
    }
}
