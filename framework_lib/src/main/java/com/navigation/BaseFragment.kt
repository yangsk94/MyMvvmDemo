package com.navigation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.BaseApplication
import com.utils.Logger
import com.widgets.ToastCompat


/**
 * Created by yangsk on 2017/5/21.
 */

abstract class BaseFragment : Fragment() {

    var TAG = javaClass.simpleName

    private var mMainHandler: Handler? = null

    protected var mIsFirstVisible = true

    @Volatile
    var isAlive: Boolean = false
        private set


    private val mStatusBar = true

    protected val mainHandler: Handler?
        get() {
            ensureMainHandler()
            return mMainHandler
        }

    override fun onResume() {
        Logger.e(TAG, "---------onResume")
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isAlive = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isVis = isHidden || userVisibleHint
        if (isVis && mIsFirstVisible) {
            lazyLoad()
            mIsFirstVisible = false
        }
    }

    /**
     * 数据懒加载
     */
    protected open fun lazyLoad() {

    }

    override fun onDestroyView() {
        isAlive = false
        destroyResource()
        super.onDestroyView()
    }

    override fun getContext(): Context? {
        var superContext: Context? = super.getActivity()
        superContext = superContext ?: super.getContext()
        return superContext ?: BaseApplication.context!!
    }

    fun runOnUiThread(action: Runnable?) {
        if (null == action) {
            return
        }
        if (Thread.currentThread() !== Looper.getMainLooper().thread) {
            ensureMainHandler()
            mMainHandler?.post(action)
        } else {
            action.run()
        }
    }

    fun removeRunnableFromMainHandler(runnable: Runnable?) {
        if (null == runnable || null == mMainHandler) {
            return
        }
        mMainHandler?.removeCallbacks(runnable)
    }

    fun runOnUiThread(action: Runnable?, delayMillis: Long) {
        if (null == action) {
            return
        }
        ensureMainHandler()
        mMainHandler?.postDelayed(action, delayMillis)
    }

    private fun ensureMainHandler() {
        if (null == mMainHandler) {
            mMainHandler = Handler(Looper.getMainLooper())
        }
    }

    fun showToast(text: String) {
        if (!isHidden && !TextUtils.isEmpty(text)) {
            context?.let { ToastCompat.makeText(it, text, Toast.LENGTH_LONG).show() }
        }
    }

    private fun destroyResource() {
        mMainHandler?.removeCallbacksAndMessages(null)
        mMainHandler = null
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isResumed) {
            onVisibilityChangedToUser(isVisibleToUser)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (userVisibleHint) {
            onVisibilityChangedToUser(!hidden)
        }
    }

    protected fun onVisibilityChangedToUser(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            onShow()
        } else {
            onHidden()
        }
    }

    protected fun onShow() {

    }

    protected fun onHidden() {

    }
}