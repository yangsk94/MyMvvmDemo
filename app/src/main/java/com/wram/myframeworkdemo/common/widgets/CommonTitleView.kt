package com.wram.myframeworkdemo.common.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.common.base.BaseFrameLayout
import com.utils.ViewUtil
import com.wram.myframeworkdemo.R
import com.wram.myframeworkdemo.databinding.ViewCommonTitleBinding


/**
 * @author yangsk
 * @class describe  [.] 公共title
 * @time 17/12/2018 11:58 AM
 */
class CommonTitleView : BaseFrameLayout<ViewCommonTitleBinding> {

    val rightTextView: TextView
        get() = mBinding.titleRightText

    val rightIconView: ImageView
        get() = mBinding.titleRightIcon

    val titleTextView: TextView
        get() = mBinding.titleTextView

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun getLayoutResId(): Int {
        return R.layout.view_common_title
    }

    override fun getLayoutResIdName(): String {
        return "view_common_title"
    }

    override fun initView() {
        ViewUtil.setOnTouch(mBinding.titleBackIcon, 0.7f)

    }

    fun setRightText(msg: String, listener: OnClickListener) {
        ViewUtil.updateViewVisibility(mBinding.titleRightText, true)
        if (null != mBinding.titleRightText) {
            mBinding.titleRightText.text = msg
            mBinding.titleRightText.setOnClickListener(listener)
        }
    }


    fun setRightTextColor(@ColorRes resourceId: Int) {
        if (null != mBinding.titleRightText) {
            mBinding.titleRightText.setTextColor(resources.getColor(resourceId))
        }
    }

    fun setBackGroundRes(res: Int) {
        mBinding.titleBgView.setBackgroundResource(res)
    }

    fun setLeftIconVisible(visible: Boolean) {
        mBinding.titleBackIcon.visibility = if (visible) VISIBLE else GONE
    }

    fun setLeftIcon(@DrawableRes icon: Int) {
        setLeftIcon(resources.getDrawable(icon), null)
    }

    fun setLeftIcon(@DrawableRes icon: Int, listener: OnClickListener) {
        setLeftIcon(resources.getDrawable(icon), listener)
    }

    @JvmOverloads
    fun setLeftIcon(drawable: Drawable, listener: OnClickListener? = null) {
        if (null != mBinding.titleBackIcon) {
            mBinding.titleBackIcon.setImageDrawable(drawable)
            mBinding.titleBackIcon.setOnClickListener(listener)
        }
    }

    fun setLeftIconListener(listener: OnClickListener) {
        if (null != mBinding.titleBackIcon) {
            mBinding.titleBackIcon.setOnClickListener(listener)
        }
    }

    fun setWhiteLeftIcon() {
        if (null != mBinding.titleBackIcon) {
            mBinding.titleBackIcon.setImageDrawable(
                ViewUtil.tintListDrawable(
                    mBinding.titleBackIcon.drawable,
                    resources.getColorStateList(R.color.white)
                )
            )
        }
    }

    fun setRightIcon(@DrawableRes icon: Int) {
        setRightIcon(resources.getDrawable(icon), null)
    }

    fun setRightIcon(@DrawableRes icon: Int, listener: OnClickListener) {
        setRightIcon(resources.getDrawable(icon), listener)
    }

    @JvmOverloads
    fun setRightIcon(drawable: Drawable, listener: OnClickListener? = null) {
        ViewUtil.updateViewVisibility(mBinding.titleRightIcon, true)
        if (null != mBinding.titleRightIcon) {
            mBinding.titleRightIcon.setImageDrawable(drawable)
            mBinding.titleRightIcon.setOnClickListener(listener)
        }
    }

    fun setRightIconListener(listener: OnClickListener) {
        if (null != mBinding.titleRightIcon) {
            mBinding.titleRightIcon.setOnClickListener(listener)
        }
    }

    fun setRightIconVisible(visible: Boolean) {
        mBinding.titleRightIcon.visibility = if (visible) VISIBLE else GONE
    }

    fun setTitleText(@StringRes resourceId: Int) {
        setTitleText(resources.getString(resourceId))
    }

    fun setTitleTextColor(@ColorRes resourceId: Int) {
        mBinding.titleTextView.setTextColor(resources.getColor(resourceId))
    }

    fun setTitleTextSize(size: Int) {
        mBinding.titleTextView.textSize = size.toFloat()
    }

    fun setTitleText(title: String) {
        mBinding.titleTextView.text = title
    }

    fun setTitleVisible(visible: Boolean) {
        mBinding.titleTextView.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun destroyResourceInner(vararg imageView: ImageView) {

        for (i in imageView.indices) {
            ViewUtil.unbindDrawable(imageView.clone()[i])
            imageView.clone()[i].setOnClickListener(null)
        }
        if (null != mBinding.titleRightText) {
            mBinding.titleRightText.setOnClickListener(null)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        destroyResource()
    }

    public override fun destroyResource() {
        destroyResourceInner(mBinding.titleBackIcon, mBinding.titleRightIcon)
    }


}
