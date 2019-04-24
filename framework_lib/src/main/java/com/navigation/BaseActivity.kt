package com.navigation

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.utils.Logger
import com.utils.ScreenUtils

/**
 * Created by yangsk on 2017/5/21.
 */

abstract class BaseActivity : FragmentActivity() {

    open val TAG = javaClass.simpleName

    var isOnResume: Boolean = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        clearSavedInstanceState(savedInstanceState)
        super.onCreate(savedInstanceState)
        window.decorView.setOnSystemUiVisibilityChangeListener { ScreenUtils.setHideVirtualKey(this@BaseActivity) }
        ScreenUtils.setHideVirtualKey(this@BaseActivity)
    }

    private fun clearSavedInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.putParcelable("android:support:fragments", null)
    }

    override fun onResume() {
        isOnResume = true
        Logger.e(TAG, "---------onResume")
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        isOnResume = false
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
