package com.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.FloatRange
import android.text.InputFilter
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.wram.base_library.R

import java.lang.reflect.Field


/**
 * Created by yangsk on 2017/6/9.
 */

class ViewUtil {

    private fun isSoftShowing(context: Activity): Boolean {
        //获取当前屏幕内容的高度
        val screenHeight = context.window.decorView.height
        //获取View可见区域的bottom
        val rect = Rect()
        context.window.decorView.getWindowVisibleDisplayFrame(rect)

        return screenHeight - rect.bottom != 0
    }

    companion object {

        fun unbindDrawable(view: View?) {
            if (null == view) {
                return
            }

            if (null != view.background) {
                view.background.callback = null
                view.background = null
            }

            if (view is ImageView) {
                val imageView = view as ImageView?
                if (null != imageView?.drawable) {
                    imageView.drawable.callback = null
                    imageView.setImageDrawable(null)
                }
            }
        }


        fun unbindDrawables(view: View?) {
            if (view != null) {
                if (view.background != null) {
                    view.background.callback = null
                }
                if (view is ImageView) {
                    val imageView = view as ImageView?
                    imageView?.setImageDrawable(null)
                }
                if (view is ViewGroup && view !is AdapterView<*>) {
                    for (i in 0 until view.childCount) {
                        unbindDrawables(view.getChildAt(i))
                    }
                    if (view !is AbsSpinner && view !is AbsListView) {
                        view.removeAllViews()
                    }
                }
            }
        }

        /**
         * 动画解绑去除监听专用
         *
         * @param animator
         */
        fun unbindAnim(animator: Animator?) {
            if (null == animator) {
                return
            }
            if (animator is AnimatorSet) {
                for (anim in animator.childAnimations) {
                    if (null != anim) {
                        anim.removeAllListeners()
                        if (anim is ValueAnimator) {
                            anim.removeAllUpdateListeners()
                        }
                        anim.cancel()
                    }
                }
            }
            animator.removeAllListeners()
            if (animator is ValueAnimator) {
                animator.removeAllUpdateListeners()
            }
            animator.cancel()
        }

        /**
         * 动画解绑去除监听专用 Animation
         *
         * @param animation
         */
        fun unbindAnim(animation: Animation?) {
            if (null == animation) {
                return
            }
            if (animation is AnimationSet) {
                for (anim in animation.animations) {
                    if (null != anim) {
                        anim.setAnimationListener(null)
                        anim.cancel()
                    }
                }
            }
            animation.setAnimationListener(null)
            animation.cancel()
        }

        fun updateWindowBackgroundAlpha(activity: Activity?, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
            if (null == activity) {
                return
            }
            val window = activity.window
            val params = window.attributes
            params.alpha = alpha
            window.attributes = params
        }

        @SuppressLint("NewApi")
        fun setBackground(view: View?, drawable: Drawable) {
            if (null == view) {
                Logger.e("view is null !!!")
                return
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.background = drawable
            } else {
                view.setBackgroundDrawable(drawable)
            }
        }

        fun updateViewVisibility(view: View?, visible: Boolean) {
            if (null != view) {
                if (visible) {
                    if (View.VISIBLE != view.visibility) {
                        view.visibility = View.VISIBLE
                    }
                } else {
                    if (View.GONE != view.visibility) {
                        view.visibility = View.GONE
                    }
                }
            }
        }

        fun updateViewVisibility(view: View?, visible: Boolean, invisible: Boolean) {
            if (invisible && null != view) {
                if (View.INVISIBLE != view.visibility) {
                    view.visibility = View.INVISIBLE
                }
                return
            }
            updateViewVisibility(view, visible)
        }

        fun updateViewVisibility(view: View, visible: Boolean, invisible: Boolean, all: Boolean) {
            updateViewVisibility(view, visible, invisible)
            if (all && view is ViewGroup) {
                var i = 0
                val count = view.childCount
                while (i < count) {
                    val childView = view.getChildAt(i)
                    updateViewVisibility(childView, visible, invisible, all)
                    i++
                }
            }
        }

        @JvmOverloads
        fun isFastDoubleClick(view: View, whatTime: Long = 800): Boolean {
            if (null == view) {
                return false
            }
            val `object` = view.getTag(R.id.fast_click_id)
            if (`object` is Long) {
                val time = System.currentTimeMillis()
                val delta = time - `object`
                if (delta > 0 && delta < whatTime) {
                    return true
                }
            }
            view.setTag(R.id.fast_click_id, System.currentTimeMillis())
            return false
        }

        fun showSoftInput(view: View?) {
            if (null == view) {
                return
            }
            val context = view.context
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }

        fun hideSoftInput(view: View?) {
            if (null == view) {
                return
            }
            val context = view.context
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun hideSoftInput(activity: Activity?) {
            if (null == activity) {
                return
            }
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocus = activity.currentFocus ?: return
            if (imm.isActive(currentFocus)) {
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            }
        }

        fun isSoftInputActive(view: View?): Boolean {
            if (null == view) {
                return false
            }
            val context = view.context
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return imm.isActive(view)
        }

        fun isFullScreen(activity: Activity): Boolean {
            return activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != 0
        }

        fun isResourceIdValid(id: Int): Boolean {
            return id.ushr(24) >= 2
        }


        @TargetApi(Build.VERSION_CODES.KITKAT)
        fun isTranslucentStatus(activity: Activity): Boolean {

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS != 0
            } else false
        }

        /**
         * 得到EditText最大长度
         *
         * @param view EditText
         * @return 最大长度
         */
        fun getMaxLength(view: EditText): Int {
            var length = 0
            try {
                val inputFilters = view.filters
                for (filter in inputFilters) {
                    val c = filter.javaClass
                    if (c.name == "android.text.InputFilter\$LengthFilter") {
                        val f = c.declaredFields
                        for (field in f) {
                            if (field.name == "mMax") {
                                field.isAccessible = true
                                length = field.get(filter) as Int
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return length
        }


        fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
            if (v.layoutParams is ViewGroup.MarginLayoutParams) {
                val p = v.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(l, t, r, b)
                v.requestLayout()
            }
        }

        fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int, height: Int) {
            if (v.layoutParams is ViewGroup.MarginLayoutParams) {
                val p = v.layoutParams as ViewGroup.MarginLayoutParams
                p.height = height
                p.setMargins(l, t, r, b)
                v.requestLayout()
            }
        }

        fun setDrawableResIcon(view: TextView, context: Context, start: Int, top: Int, end: Int, bottom: Int) {
            view.setCompoundDrawablesRelativeWithIntrinsicBounds(
                getDrawable(context, start), getDrawable(context, top),
                getDrawable(context, end), getDrawable(context, bottom)
            )
        }

        fun setDrawableResIconTop(view: RadioButton, context: Context, start: Int, top: Int, end: Int, bottom: Int) {
            view.setCompoundDrawablesRelativeWithIntrinsicBounds(
                getDrawable(context, start), getDrawable(context, top),
                getDrawable(context, end), getDrawable(context, bottom)
            )
        }


        fun getDrawable(context: Context, res: Int): Drawable? {
            return if (res == 0) null else context.resources.getDrawable(res)
        }
    }
}
