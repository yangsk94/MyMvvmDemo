package com.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.navigation.BaseViewModel;
import com.navigation.PageFragment;
import com.trello.rxlifecycle2.LifecycleTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/23 下午4:50
 */
public abstract class BaseCommonFragment<B extends ViewDataBinding, VM extends BaseViewModel> extends PageFragment {

    public B mBinding;

    public VM mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getIntentParams();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mBinding = DataBindingUtil.bind(view);
        mViewModel = mViewModel == null ? createViewModel() : mViewModel;
        if (mViewModel == null || getVariableId() == 0) return view;
        mBinding.setVariable(getVariableId(), mViewModel);
        mBinding.executePendingBindings();
        return view;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mViewModel != null) mViewModel.create();
        initGlobalParams();
    }

    public void initGlobalParams() {

    }

    public void getIntentParams() {

    }

    protected abstract int getLayoutId();


    protected abstract VM createViewModel();

    protected abstract int getVariableId();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewModel != null) {
            mViewModel.onDestroy();
            mViewModel = null;
        }
    }

}
