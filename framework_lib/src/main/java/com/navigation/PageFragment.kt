package com.navigation

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.view.KeyEvent
import com.utils.TaskEngine

import java.util.TimerTask


/**
 * Created by yangsk on 2017/5/21.
 */

abstract class PageFragment() : BaseFragment() {

    var transitionStyle: Int = 0

    fun finish() {
        if (isAlive) {
            Navigation.back(this)
        }
    }

    @JvmOverloads
    fun setResult(resultCode: Int, data: Intent? = null) {
        val activity = activity
        if (activity is Navigation.NavigationInterface) {
            val navigationInterface = activity as Navigation.NavigationInterface?
            if (data != null) {
                navigationInterface?.setResult(resultCode, data)
            }
        } else if (activity is FragmentActivity) {
            activity.setResult(resultCode, data)
        } else {
            throw RuntimeException("setResult in PageFragment has error !")
        }
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }
}
