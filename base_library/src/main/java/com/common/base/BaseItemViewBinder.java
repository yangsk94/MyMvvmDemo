package com.common.base;

import android.content.Context;
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
    public B mBinding;
    public D item;
    public Context context;
    public int mAdapterPosition, mLayoutPosition;


    @NonNull
    @Override
    protected BaseViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        this.context = parent.getContext();
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutResId(), parent, false);
        return new BaseViewHolder(mBinding.getRoot());
    }

    @LayoutRes
    public abstract int getLayoutResId();

    public abstract int getVariableId();

    public void setData() {

    }


    @Override
    protected void onBindViewHolder(@NonNull BaseViewHolder holder, @NonNull D item) {
        this.item = item;
        holder.mBinding.setVariable(getVariableId(), item);
        holder.mBinding.executePendingBindings();

        setData();
        mAdapterPosition = holder.getAdapterPosition();
        mLayoutPosition = holder.getLayoutPosition();
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding mBinding;


        BaseViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);

        }

    }

}
