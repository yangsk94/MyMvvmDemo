package com.utils

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.*
import android.widget.TextView

/**
 * 文字效果
 */
class TextStyleUtil {

    var spannableString: SpannableStringBuilder? = null

    constructor() {}

    constructor(str: String) {
        spannableString = SpannableStringBuilder(str)
    }

    /**
     * 改变文字大小dp
     *
     * @param size
     * @param start
     * @param end
     * @param dp    true为size的单位是dip，false为px。
     * @return
     */
    fun setAbsoluteSize(size: Int, start: Int, end: Int, dp: Boolean): TextStyleUtil {
        spannableString?.setSpan(
            AbsoluteSizeSpan(size, dp),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        ) // 第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
        return this
    }

    /**
     * 改变相对文字大小
     */
    fun setRelativeSize(size: Float, start: Int, end: Int): TextStyleUtil {
        spannableString?.setSpan(RelativeSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置前景色
     */

    fun setForegroundColor(Color: Int, start: Int, end: Int): TextStyleUtil {
        if (spannableString == null) {
            return this
        }
        spannableString?.setSpan(ForegroundColorSpan(Color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    /**
     * 设置背景色
     */
    fun setBackgroundColor(Color: Int, start: Int, end: Int): TextStyleUtil {
        if (spannableString == null) {
            return this
        }
        spannableString?.setSpan(BackgroundColorSpan(Color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) // 设置前景色为洋红色
        return this
    }

    /**
     * 下划线
     *
     * @param start
     * @param end
     * @return
     */
    fun setUnderlineSpan(start: Int, end: Int): TextStyleUtil {
        if (spannableString == null) {
            return this
        }
        spannableString?.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) // 设置前景色为洋红色
        return this
    }

    /**
     * 删除线
     */
    fun setStrikethroughSpan(start: Int, end: Int): TextStyleUtil {
        if (spannableString == null) {
            return this
        }
        spannableString?.setSpan(StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) // 设置前景色为洋红色
        return this
    }

    /**
     * 上标
     *
     * @param start
     * @param end
     * @return
     */
    fun setSubscriptSpan(start: Int, end: Int): TextStyleUtil {
        if (spannableString == null) {
            return this
        }
        spannableString?.setSpan(SubscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) // 设置前景色为洋红色
        return this
    }

    /**
     * 下标
     *
     * @param start
     * @param end
     * @return
     */
    fun setSuperscriptSpan(start: Int, end: Int): TextStyleUtil {
        if (spannableString == null) {
            return this
        }
        spannableString?.setSpan(SuperscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) // 设置前景色为洋红色
        return this
    }

    /**
     * 文字设置样式（正常、粗体、斜体、粗斜体）。
     * Typeface.NORMAL、Typeface.BOLD、Typeface.ITALIC、Typeface.BOLD_ITALIC
     *
     * @param start
     * @param end
     * @param typeface
     * @return
     */
    fun setStyleSpan(start: Int, end: Int, typeface: Int): TextStyleUtil {
        if (spannableString == null) {
            return this
        }
        spannableString?.setSpan(StyleSpan(typeface), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    companion object {

        fun setFakeBold(textView: TextView, isBold: Boolean) {
            val tp = textView.paint
            tp.isFakeBoldText = isBold
        }

        /**
         * @param size =-1不设置字体形状
         */
        fun setTextStyle(
            context: Context,
            view: TextView,
            str: String,
            size: Int,
            start: Int,
            end: Int,
            color: Int,
            typeface: Int
        ) {
            if (TextUtils.isEmpty(str)) {
                return
            }
            val styleUtil = TextStyleUtil(str)//本次消费
            if (size != -1) {
                styleUtil.setAbsoluteSize(size, start, end, true)
            }
            styleUtil.setForegroundColor(context.resources.getColor(color), start, end)
            if (typeface != -1) {
                styleUtil.setStyleSpan(start, end, typeface)
            }
            view.text = styleUtil.spannableString
        }

        fun setTextStyle(
            context: Context,
            view: TextView,
            str: String,
            size: Int,
            start: Int,
            end: Int,
            color: Int,
            typeface: Int,
            underline: Boolean
        ) {
            if (TextUtils.isEmpty(str)) {
                return
            }
            val styleUtil = TextStyleUtil(str)
            if (size != -1) {
                styleUtil.setAbsoluteSize(size, start, end, true)
            }
            styleUtil.setForegroundColor(context.resources.getColor(color), start, end)
            if (typeface != -1) {
                styleUtil.setStyleSpan(start, end, typeface)
            }
            if (underline) {
                styleUtil.setUnderlineSpan(end, str.length)
            }
            view.text = styleUtil.spannableString

        }
    }
}
