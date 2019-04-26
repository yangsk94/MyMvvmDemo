package com.wram.myframeworkdemo.content

import com.common.base.BaseItemViewBinder
import com.utils.ImageLoader
import com.wram.myframeworkdemo.BR
import com.wram.myframeworkdemo.R
import com.wram.myframeworkdemo.databinding.BinderItemContentBinding

/**
 * @author ysk
 * @class describe  [.]
 * @time 2019/4/25 上午10:26
 */
class ContentBinder : BaseItemViewBinder<ContentBean, BinderItemContentBinding>() {

    override fun getLayoutResId(): Int {
        return R.layout.binder_item_content
    }

    override fun getVariableId(): Int {
        return BR.data
    }


    override fun setData(
        adapterPosition: Int, layoutPosition: Int, item: ContentBean
        , binding: BinderItemContentBinding
    ) {

        if (adapterPosition == 10) {
            ImageLoader.loadImage(binding.image, "https:" + item.hint!!, R.mipmap.car_img)
        } else {
            ImageLoader.loadImage(binding.image, "https:" + item.hint!!)
        }

    }
}
