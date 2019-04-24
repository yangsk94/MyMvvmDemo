package com.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import com.navigation.BaseActivity;
import com.navigation.BaseViewModel;
import com.widgets.ToastCompat;
import org.jetbrains.annotations.Nullable;

/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/23 下午4:50
 */
public abstract class BaseCommonActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity {


    private B mBinding;
    private VM mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getIntentParams();
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        if (getVariableId() != 0) {
            mBinding.setVariable(getVariableId(), mViewModel);
            mBinding.executePendingBindings();
        }
        mViewModel.create();
        initGlobalParams();
    }

    private void initGlobalParams() {

    }


    private void getIntentParams() {

    }

    protected abstract int getLayoutId();

    protected abstract VM createViewModel();

    protected abstract int getVariableId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewModel != null) {
            mViewModel.onDestroy();
            mViewModel = null;
        }
    }

    public void showToast(String string) {
        ToastCompat.Companion.showToast(this, string);
    }

    public void showToast(int string) {
        ToastCompat.Companion.showToast(this, String.valueOf(string));
    }
}
