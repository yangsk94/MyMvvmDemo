package com.wram.myframeworkdemo.content;

import com.common.base.BaseItemViewBinder;
import com.utils.ImageLoader;
import com.wram.myframeworkdemo.BR;
import com.wram.myframeworkdemo.R;
import com.wram.myframeworkdemo.databinding.BinderItemContentBinding;

/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/25 上午10:26
 */
public class ContentBinder extends BaseItemViewBinder<ContentBean, BinderItemContentBinding> {

    @Override
    public int getLayoutResId() {
        return R.layout.binder_item_content;
    }

    @Override
    public int getVariableId() {
        return BR.data;
    }

    @Override
    public void setData() {
        super.setData();
        if (mLayoutPosition == 10) {
            ImageLoader.INSTANCE.loadImage(mBinding.image, "https:" + item.getHint(), R.mipmap.car_img);
            return;
        }
        ImageLoader.INSTANCE.loadImage(mBinding.image, "https:" + item.getHint());
    }
}