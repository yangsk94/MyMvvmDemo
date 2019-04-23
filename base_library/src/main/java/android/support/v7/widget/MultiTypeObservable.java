package android.support.v7.widget;

/**
 * 修复：Cannot call this method while RecyclerView is computing a layout or scrolling的bug<br/>
 * 详见：<a href='https://bugly.qq.com/v2/crash-reporting/errors/1400010551/22771/report?pid=1&search=XcbbCatchedException&searchType=detail&bundleId=&channelId=&version=1.4.2&tagList=&start=0&date=all'>这里</a>
 * <br/><br/>
 *
 * @author yangsk
 * @date 2018/2/6
 */

class MultiTypeObservable extends RecyclerView.AdapterDataObservable {

    private static final int TYPE_NOTIFY_CHANGED               = 0;
    private static final int TYPE_NOTIFY_ITEM_RANGE_CHANGED_1  = 1;
    private static final int TYPE_NOTIFY_ITEM_RANGE_CHANGED_2  = 2;
    private static final int TYPE_NOTIFY_ITEM_RANGE_INSERTED   = 3;
    private static final int TYPE_NOTIFY_ITEM_RANGE_REMOVED    = 4;
    private static final int TYPE_NOTIFY_ITEM_MOVED            = 5;

    private static final int MAX_DELAY_COUNT = 3;

    private LayoutObserver mLayoutObserver;

    MultiTypeObservable() {
        super();
    }

    void registerLayoutObserver(LayoutObserver observer) {
        this.mLayoutObserver = observer;
    }

    @Override
    public boolean hasObservers() {
        return super.hasObservers();
    }

    @Override
    public void notifyChanged() {
        MultiTypeHandler.getIMPL().runOnUiThread(new InternalRunnable(TYPE_NOTIFY_CHANGED, 0, 0, 0, 0, null));
    }

    @Override
    public void notifyItemRangeChanged(final int positionStart, final int itemCount) {
        MultiTypeHandler.getIMPL().runOnUiThread(new InternalRunnable(TYPE_NOTIFY_ITEM_RANGE_CHANGED_1, 0, 0, positionStart, itemCount, null));
    }

    @Override
    public void notifyItemRangeChanged(final int positionStart, final int itemCount, final Object payload) {
        MultiTypeHandler.getIMPL().runOnUiThread(new InternalRunnable(TYPE_NOTIFY_ITEM_RANGE_CHANGED_2, 0, 0, positionStart, itemCount, payload));
    }

    @Override
    public void notifyItemRangeInserted(final int positionStart, final int itemCount) {
        MultiTypeHandler.getIMPL().runOnUiThread(new InternalRunnable(TYPE_NOTIFY_ITEM_RANGE_INSERTED, 0, 0, positionStart, itemCount, null));
    }

    @Override
    public void notifyItemRangeRemoved(final int positionStart, final int itemCount) {
        MultiTypeHandler.getIMPL().runOnUiThread(new InternalRunnable(TYPE_NOTIFY_ITEM_RANGE_REMOVED, 0, 0, positionStart, itemCount, null));
    }

    @Override
    public void notifyItemMoved(final int fromPosition, final int toPosition) {
        MultiTypeHandler.getIMPL().runOnUiThread(new InternalRunnable(TYPE_NOTIFY_ITEM_MOVED, fromPosition, toPosition, 0, 0, null));
    }

    private class InternalRunnable implements Runnable {

        private final int type;

        private int fromPosition;
        private int toPosition;
        private int positionStart;
        private int itemCount;
        private Object payload;

        private int count;

        InternalRunnable(int type, int fromPosition, int toPosition, int positionStart, int itemCount, Object payload) {
            this.type = type;
            this.fromPosition = fromPosition;
            this.toPosition = toPosition;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
            this.payload = payload;
        }

        @Override
        public void run() {
            if (null != mLayoutObserver && mLayoutObserver.isComputingLayout() && (++count <= MAX_DELAY_COUNT)) {
                MultiTypeHandler.getIMPL().runOnUiThread(this, count * 50);
                return;
            }
            if (TYPE_NOTIFY_CHANGED == type) {
                MultiTypeObservable.super.notifyChanged();
            } else if (TYPE_NOTIFY_ITEM_RANGE_CHANGED_1 == type) {
                MultiTypeObservable.super.notifyItemRangeChanged(positionStart, itemCount);
            } else if (TYPE_NOTIFY_ITEM_RANGE_CHANGED_2 == type) {
                MultiTypeObservable.super.notifyItemRangeChanged(positionStart, itemCount, payload);
            } else if (TYPE_NOTIFY_ITEM_RANGE_INSERTED == type) {
                MultiTypeObservable.super.notifyItemRangeInserted(positionStart, itemCount);
            } else if (TYPE_NOTIFY_ITEM_RANGE_REMOVED == type) {
                MultiTypeObservable.super.notifyItemRangeRemoved(positionStart, itemCount);
            } else if (TYPE_NOTIFY_ITEM_MOVED == type) {
                MultiTypeObservable.super.notifyItemMoved(fromPosition, toPosition);
            }
        }
    }

    /**
     * 修复：Cannot call this method while RecyclerView is computing a layout or scrolling的bug<br/>
     * 详见：<a href='https://bugly.qq.com/v2/crash-reporting/errors/1400010551/22771/report?pid=1&search=XcbbCatchedException&searchType=detail&bundleId=&channelId=&version=1.4.2&tagList=&start=0&date=all'>这里</a>
     * <br/><br/>
     *
     * @author ysk
     * @date 2018/2/11
     */
    public interface LayoutObserver {
        boolean isComputingLayout();
    }
}
