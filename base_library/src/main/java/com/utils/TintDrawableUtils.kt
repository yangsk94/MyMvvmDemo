package com.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat

object TintDrawableUtils {

    /**
     * 对目标Drawable 进行着色
     *
     * @param drawable 目标Drawable
     * @param color    着色的颜色值
     * @return 着色处理后的Drawable
     */
    fun tintDrawable(drawable: Drawable, color: Int): Drawable {
        // 获取此drawable的共享状态实例
        val wrappedDrawable = getCanTintDrawable(drawable)
        // 进行着色
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }

    /**
     * 对目标Drawable 进行着色。
     * 通过ColorStateList 指定单一颜色
     *
     * @param drawable 目标Drawable
     * @param color    着色值
     * @return 着色处理后的Drawable
     */
    fun tintListDrawable(drawable: Drawable, color: Int): Drawable {
        return tintListDrawable(drawable, ColorStateList.valueOf(color))
    }

    /**
     * 对目标Drawable 进行着色
     *
     * @param drawable 目标Drawable
     * @param colors   着色值
     * @return 着色处理后的Drawable
     */
    fun tintListDrawable(drawable: Drawable, colors: ColorStateList): Drawable {
        val wrappedDrawable = getCanTintDrawable(drawable)
        // 进行着色
        DrawableCompat.setTintList(wrappedDrawable, colors)
        return wrappedDrawable
    }

    /**
     * 获取可以进行tint 的Drawable
     *
     *
     * 对原drawable进行重新实例化  newDrawable()
     * 包装  warp()
     * 可变操作 mutate()
     *
     * @param drawable 原始drawable
     * @return 可着色的drawable
     */
    private fun getCanTintDrawable(drawable: Drawable): Drawable {
        // 获取此drawable的共享状态实例
        val state = drawable.constantState
        // 对drawable 进行重新实例化、包装、可变操作
        return DrawableCompat.wrap(if (state == null) drawable else state.newDrawable()).mutate()
    }
}