package com.wram.myframeworkdemo.content;

import android.databinding.ViewDataBinding;
import com.common.base.BaseItemViewBinder;
import com.wram.myframeworkdemo.BR;
import com.wram.myframeworkdemo.R;

/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/25 上午10:26
 */
public class ContentBinder extends BaseItemViewBinder<ContentBean, ViewDataBinding> {

    @Override
    public int getLayoutResId() {
        return R.layout.binder_item_content;
    }

    @Override
    public int getVariableId() {
        return BR.data;
    }
}