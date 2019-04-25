package com.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ItemViewBinder;
import android.support.v7.widget.MultiTypeAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import com.common.base.BaseFrameLayout;
import com.widgets.ptr.PtrFrameLayout;
import com.widgets.ptr.PtrHandler;
import com.wram.base_library.R;
import com.wram.base_library.databinding.RecyclerCommonBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/22 下午4:29
 */
public class RefreshRecyclerView extends BaseFrameLayout<RecyclerCommonBinding> implements PtrHandler {

    private MultiTypeAdapter mAdapter;

    private List<Object> mList;

    private boolean mLoadingState;

    private int mCurPage;

    private OnScrollListener addOnScrollListener;

    private boolean mRefreshEnabled;

    private boolean mMoreEnabled;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.recycler_common;
    }

    @NotNull
    @Override
    protected String getLayoutResIdName() {
        return "recycler_common";
    }

    @Override
    protected void initView() {
        mBinding.refreshLayout.setPtrHandler(this);
        mCurPage = 1;

        mBinding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (mMoreEnabled && newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (mBinding.recycler != null && !mLoadingState && !recyclerView.canScrollVertically(1)) {
                        mCurPage += 1;
                        mLoadingState = true;
                        if (addOnScrollListener != null) addOnScrollListener.onMore();
                    }
                }
            }
        });

        initRecyclerView();

    }

    private void initRecyclerView() {
        mAdapter = new MultiTypeAdapter();
        mList = new ArrayList<>();

        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setAdapter(mAdapter);
        mAdapter.setItems(mList);
    }

    public void initLayoutManager(RecyclerView.LayoutManager manager) {
        mBinding.recycler.setLayoutManager(manager);

    }

    public <T> void register(@NonNull Class<? extends T> clazz, @NonNull ItemViewBinder<T, ?> binder) {
        mAdapter.register(clazz, binder);
    }

    public void setPullRefreshEnabled(boolean enabled) {//设置是否可以刷新
        mRefreshEnabled = enabled;
    }

    public void setLoadingMoreEnabled(boolean enabled) {//设置是否可以上拉
        mMoreEnabled = enabled;
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return mRefreshEnabled && null != mBinding.recycler && !mBinding.recycler.canScrollVertically(-1) && !mLoadingState;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mLoadingState = true;
        mCurPage = 1;
        if (addOnScrollListener != null) addOnScrollListener.onRefresh(mCurPage);
    }


    public interface OnScrollListener {
        void onRefresh(int curPage);

        void onMore();
    }

    public void addOnScrollListener(OnScrollListener addOnScrollListener) {
        this.addOnScrollListener = addOnScrollListener;
    }

    public void refreshComplete() {
        if (mBinding.refreshLayout != null && mBinding.refreshLayout.isRefreshing()) {
            mBinding.refreshLayout.refreshComplete();
            mLoadingState = false;
        }
    }

    public List<Object> getList() {
        return mList;
    }

    public void setList(List<Object> mList) {
        this.mList = mList;
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
