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
import com.utils.ViewUtil;

/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/25 下午12:13
 */
public abstract class BaseItemViewBinder<D, B extends ViewDataBinding> extends ItemViewBinder<D, BaseItemViewBinder.BaseViewHolder> {

    public Context context;

    @NonNull
    @Override
    protected BaseViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        this.context = parent.getContext();
        B mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutResId(), parent, false);
        return new BaseViewHolder(mBinding.getRoot());
    }

    @LayoutRes
    public abstract int getLayoutResId();

    public abstract int getVariableId();

    public void setData(int adapterPosition, int layoutPosition, D item, B binding) {

    }

    @Override
    protected void onBindViewHolder(@NonNull BaseViewHolder holder, @NonNull D item) {
        try {
            holder.setData(item, getVariableId());
            setData(holder.getAdapterPosition(), holder.getLayoutPosition(), item, (B) holder.mBinding);
        } catch (Throwable e) {
            if (ViewUtil.Companion.isDebuggable(context)) {
                throw new RuntimeException(e);
            } else {
                e.printStackTrace();
            }
        }
    }

    static class BaseViewHolder<D, B extends ViewDataBinding> extends RecyclerView.ViewHolder {

        B mBinding;

        BaseViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);

        }

        void setData(D d, int variableId) {
            mBinding.setVariable(variableId, d);
            mBinding.executePendingBindings();
        }
    }

}
