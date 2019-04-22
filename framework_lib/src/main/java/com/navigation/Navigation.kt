package com.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.BaseApplication
import com.helper.NavigationHelper
import com.utils.Logger
import com.wram.framework_lib.R


/**
 * Created by yangsk on 2017/5/21.
 */

object Navigation {

    private val TAG = "Navigation"

    private val DEFAULT_REQUEST_CODE = -1

    val FRAGMENT_NAME = "Fragment_Name"
    val FRAGMENT_ARGS = "Fragment_Args"

    val TRANSLATION_STYLE_NAME = "Translation_Style"

    val DEFAULT_NAVIGATION_TRANSITION_STYLE_UNDEFINED = -1
    val DEFAULT_NAVIGATION_TRANSITION_STYLE_NONE = 0
    val DEFAULT_NAVIGATION_TRANSITION_STYLE_PUSH = 1
    val DEFAULT_NAVIGATION_TRANSITION_STYLE_PRESENT = 2


    fun back(fragment: PageFragment?) {
        if (null == fragment) {
            Logger.e(TAG, "fragment is null when perform back() function")
            return
        }
        val activity = fragment.activity
        if (activity is NavigationInterface) {
            val navigationInterface = activity as NavigationInterface?
            navigationInterface?.back(fragment)
        } else if (null != activity) {
            activity.finish()
        } else {
            val exception = IllegalArgumentException("can't perform back() function !")
            if (BaseApplication.context?.isDebuggable!!) {
                throw exception
            } else {
                try {//上报
                    Logger.e(TAG, "can't perform back() function !")
                } catch (throwable: Throwable) {
                }

            }
        }
    }

    fun navigationOpen(context: Context, fragment: Class<*>?, args: Bundle? = null) {
        if (null != fragment) {
            navigationOpen(context, fragment.name, args)
        } else {
            Logger.e("fragment is null !!!")
        }
    }

    fun navigationOpen(context: Context?, fragment: String, args: Bundle? = null) {
        val navigationInterface = getNavigation(context)
        if (null != navigationInterface) {
            navigationInterface.open(fragment, args)
        } else if (context is FragmentActivity) {
            open(context, fragment, args)
        } else {
            Logger.e(
                TAG,
                "context must instanceof FragmentActivity or NavigationInterface when perform navigationOpen() function !"
            )
        }
    }

    fun navigationOpen(context: Context, intent: Intent) {
        val navigationInterface = getNavigation(context)
        if (null != navigationInterface) {
            navigationInterface.open(intent)
        } else if (context is FragmentActivity) {
            open(context, intent)
        } else {
            Logger.e(
                TAG,
                "context must instanceof FragmentActivity or NavigationInterface when perform navigationOpen() function !"
            )
        }
    }

    fun navigationOpenForResult(context: Context, fragment: String, requestCode: Int) {
        navigationOpenForResult(context, fragment, null, requestCode)
    }

    fun navigationOpenForResult(context: Context, fragment: String, args: Bundle?, requestCode: Int) {
        val navigationInterface = getNavigation(context)
        if (null != navigationInterface) {
            navigationInterface.openForResult(fragment, args, requestCode)
        } else if (context is FragmentActivity) {
            openForResult(context, fragment, args, requestCode)
        } else {
            Logger.e(
                TAG,
                "context must instanceof FragmentActivity or NavigationInterface when perform navigationOpenForResult() function !"
            )
        }
    }

    fun navigationOpenForResult(context: Context, intent: Intent, requestCode: Int) {
        val navigationInterface = getNavigation(context)
        if (null != navigationInterface) {
            navigationInterface.openForResult(intent, requestCode)
        } else if (context is FragmentActivity) {
            openForResult(context, intent, requestCode)
        } else {
            Logger.e(
                TAG,
                "context must instanceof FragmentActivity or NavigationInterface when perform navigationOpenForResult() function !"
            )
        }
    }

    internal fun open(activity: FragmentActivity, fragment: String, args: Bundle?) {
        val intent = generateIntent(activity, fragment, args, DEFAULT_NAVIGATION_TRANSITION_STYLE_NONE)
        startActivity(activity, intent, DEFAULT_REQUEST_CODE, DEFAULT_NAVIGATION_TRANSITION_STYLE_NONE)
    }

    internal fun open(activity: FragmentActivity, intent: Intent) {
        startActivity(activity, intent, DEFAULT_REQUEST_CODE, DEFAULT_NAVIGATION_TRANSITION_STYLE_NONE)
    }

    internal fun openForResult(activity: FragmentActivity, fragment: String, args: Bundle?, requestCode: Int) {
        val intent = generateIntent(activity, fragment, args, requestCode)
        startActivity(activity, intent, requestCode, DEFAULT_NAVIGATION_TRANSITION_STYLE_NONE)
    }

    internal fun openForResult(activity: FragmentActivity, intent: Intent, requestCode: Int) {
        startActivity(activity, intent, requestCode, DEFAULT_NAVIGATION_TRANSITION_STYLE_NONE)
    }

    private fun startActivity(activity: FragmentActivity?, intent: Intent, requestCode: Int, transitionStyle: Int) {
        if (null == activity) {
            Logger.e(TAG, "activity is null when perform startActivity() function")
            return
        }
        activity.startActivityForResult(intent, requestCode)
        overridePendingTransition(activity, transitionStyle)
    }

    private fun generateIntent(
        activity: FragmentActivity,
        fragment: String,
        args: Bundle?,
        transitionStyle: Int
    ): Intent {
        val intent = Intent(activity, ContainerActivity::class.java)
        if (null != args) {
            val value = fragment + FRAGMENT_ARGS
            NavigationHelper.put(value, args)
            intent.putExtra(FRAGMENT_ARGS, value)
        }
        intent.putExtra(FRAGMENT_NAME, fragment)
        intent.putExtra(TRANSLATION_STYLE_NAME, transitionStyle)
        return intent
    }

    internal fun overridePendingTransition(activity: FragmentActivity?, transitionStyle: Int) {
        if (null == activity) {
            Logger.e(TAG, "activity is null when perform overridePendingTransition() function")
            return
        }
        when (transitionStyle) {
            DEFAULT_NAVIGATION_TRANSITION_STYLE_PUSH ->
                // TODO add impl
                activity.overridePendingTransition(0, R.anim.back_exit)
            DEFAULT_NAVIGATION_TRANSITION_STYLE_PRESENT ->
                // TODO add impl
                activity.overridePendingTransition(0, 0)
            DEFAULT_NAVIGATION_TRANSITION_STYLE_NONE//默认动画
            ->
                // TODO add impl
                //                activity.overridePendingTransition(0, 0);
                activity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit)
            DEFAULT_NAVIGATION_TRANSITION_STYLE_UNDEFINED -> activity.overridePendingTransition(0, 0)
            else -> activity.overridePendingTransition(0, 0)
        }
    }

    private fun getNavigation(context: Context?): NavigationInterface? {
        return if (context is NavigationInterface) {
            context
        } else null
    }

    interface NavigationInterface {

        fun open(intent: Intent)

        fun open(fragment: String, args: Bundle?)

        fun back(fragment: PageFragment)

        fun openForResult(fragment: String, args: Bundle?, requestCode: Int)

        fun openForResult(intent: Intent, requestCode: Int)

        fun setResult(requestCode: Int)

        fun setResult(resultCode: Int, data: Intent)
    }
}

