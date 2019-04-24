package com.navigation

import android.content.Context
import android.databinding.BaseObservable

abstract class BaseViewModel(context: Context) : BaseObservable() {

    val context: Context? = context

    fun create() {

    }

    fun onDestroy() {

    }

}
