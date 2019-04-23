package android.support.v7.widget;

import android.os.Handler;
import android.os.Looper;
import com.utils.Logger;


/**
 * 尝试解决RecyclerView的crash：<br/>
 * Inconsistency detected. Invalid item position 1(offset:1).state:5
 * <br/><br/>
 *
 * @author yangsk
 * @date 2018/2/6
 */

class MultiTypeHandler {

    private Handler mMainHandler;

    public static MultiTypeHandler getIMPL() {
        return Instance.INSTANCE;
    }

    public void runOnUiThread(Runnable runnable) {
        runOnUiThread(runnable, 0);
    }

    public void runOnUiThread(Runnable runnable, long delayMillis) {
        if (null == runnable) return;
        if (null == mMainHandler) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        Logger.INSTANCE.e(getClass().getSimpleName(), "runOnUiThread(" + runnable + ")");
        mMainHandler.postDelayed(runnable, delayMillis);
    }

    private static class Instance {
        private static MultiTypeHandler INSTANCE = new MultiTypeHandler();
    }
}
