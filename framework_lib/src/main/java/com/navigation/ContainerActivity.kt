package com.navigation

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.KeyEvent
import com.helper.FragmentHelper
import com.helper.NavigationHelper


/**
 * Created by yangsk on 2017/5/21.
 */

class ContainerActivity : BaseActivity(), Navigation.NavigationInterface {

    private var mCurrentFragment: Fragment? = null

    private var mTransitionStyle: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGlobalParams(savedInstanceState)
    }

    private fun initGlobalParams(savedInstanceState: Bundle?) {
        val intent = intent
        if (null != intent) {
            mTransitionStyle = intent.getIntExtra(
                Navigation.TRANSLATION_STYLE_NAME,
                Navigation.DEFAULT_NAVIGATION_TRANSITION_STYLE_NONE
            )
            if (null == savedInstanceState) {
                var fname = intent.getStringExtra(Navigation.FRAGMENT_NAME)
                if (TextUtils.isEmpty(fname)) {
                    // TODO this is a default error fragment
                    fname = ErrorFragment::class.java.name
                }
                var args: Bundle? = null
                if (intent.getStringExtra(Navigation.FRAGMENT_ARGS) != null) {
                    args = NavigationHelper.get(intent.getStringExtra(Navigation.FRAGMENT_ARGS)) as Bundle
                }
                mCurrentFragment = FragmentHelper.instantiate(this, fname, args)
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(android.R.id.content, mCurrentFragment!!, fname)
                fragmentTransaction.commit()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCurrentFragment?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        var isHandled = false
        if (mCurrentFragment is PageFragment) {
            val pageFragment = mCurrentFragment as PageFragment?
            if (pageFragment != null) {
                isHandled = pageFragment.onKeyDown(keyCode, event)
            }
        }
        return if (isHandled) isHandled else super.onKeyDown(keyCode, event)
    }

    override fun open(intent: Intent) {
        Navigation.open(this, intent)
    }

    override fun back(fragment: PageFragment) {
        finish()
    }

    override fun open(fragment: String, args: Bundle?) {
        Navigation.open(this, fragment, args)
    }

    override fun openForResult(fragment: String, args: Bundle?, requestCode: Int) {
        Navigation.openForResult(this, fragment, args, requestCode)
    }

    override fun openForResult(intent: Intent, requestCode: Int) {
        Navigation.openForResult(this, intent, requestCode)
    }

    override fun finish() {
        super.finish()
        if (mCurrentFragment is PageFragment) {
            val pageFragment = mCurrentFragment as PageFragment?
            val style = pageFragment?.transitionStyle
            if (Navigation.DEFAULT_NAVIGATION_TRANSITION_STYLE_UNDEFINED != style) {
                mTransitionStyle = style!!
            }
        }

        Navigation.overridePendingTransition(this, mTransitionStyle)
    }

    override fun onDestroy() {
        destroyResource()
        super.onDestroy()
    }

    protected fun destroyResource() {
        if (null != mCurrentFragment) {
            mCurrentFragment = null
        }
    }


}