package com.common.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.ItemViewBinder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/25 下午12:13
 */
public abstract class BaseItemViewBinder<D, B extends ViewDataBinding> extends ItemViewBinder<D, BaseItemViewBinder.BaseViewHolder> {


    @NonNull
    @Override
    protected BaseViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        B itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                getLayoutResId(), parent, false);

        return new BaseViewHolder(itemBinding.getRoot());

    }

    @LayoutRes
    public abstract int getLayoutResId();

    public abstract int getVariableId();

    @Override
    protected void onBindViewHolder(@NonNull BaseViewHolder holder, @NonNull D item) {
        holder.mBinding.setVariable(getVariableId(), item);
        holder.mBinding.executePendingBindings();
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding mBinding;


        BaseViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);

        }

    }

}
