package com.wram.myframeworkdemo.content

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.navigation.BaseViewModel
import com.wram.myframeworkdemo.R

/**
 * @author ysk
 * @class describe  [.]
 * @time 2019/4/22 上午10:35
 */
class ContentVM(context: Context) : BaseViewModel(context) {

    var data = ObservableField<String>()


    @BindingAdapter("app:imageUrl")
    fun bindSubjectIcon(imageView: ImageView, path: String) {
        if (!TextUtils.isEmpty(path)) {
            Glide
                .with(imageView.context)
                .asBitmap()
                .load("https:$path")
                .apply(
                    RequestOptions().error(R.mipmap.car_img)
                        .placeholder(R.mipmap.car_img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                )
                .into(imageView)
        } else {
            Glide.with(imageView.context)
                .asBitmap()
                .load(R.mipmap.car_img)
                .into(imageView)
        }
    }
}
