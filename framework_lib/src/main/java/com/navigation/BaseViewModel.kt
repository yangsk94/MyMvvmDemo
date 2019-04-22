package com.navigation

import android.content.Context
import android.databinding.BaseObservable
import com.trello.rxlifecycle2.LifecycleTransformer

abstract class BaseViewModel : BaseObservable {

    private val activity: BaseActivity?

    private val fragment: BaseFragment?

    val context: Context?
        get() {
            if (activity != null) {
                return activity
            }
            if (fragment != null) {
                return fragment.context
            }
            throw IllegalStateException("No view attached")
        }

    constructor(activity: BaseActivity) {
        this.activity = activity
        fragment = null
    }

    constructor(fragment: BaseFragment) {
        this.fragment = fragment
        activity = null
    }


    fun getActivity(): BaseActivity {
        if (activity != null) {
            return activity
        }
        throw IllegalStateException("No view attached")
    }

    fun <T> bindToLifecycleAct(): LifecycleTransformer<T> {
        if (activity != null) {
            return activity.bindToLifecycle()
        }
        throw IllegalStateException("No view attached")
    }


    fun <T> bindToLifecycleFragment(): LifecycleTransformer<T> {
        if (fragment != null) {
            return fragment.bindToLifecycle()
        }
        throw IllegalStateException("No view attached")
    }

    fun create() {

    }

    fun onDestroy() {

    }

}
