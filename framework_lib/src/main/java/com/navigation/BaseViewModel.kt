package com.navigation

import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.databinding.BaseObservable
import android.databinding.ObservableBoolean
import com.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.utils.Logger

abstract class BaseViewModel(context: Context) : BaseObservable() {

    val context: Context? = context

    val isShowRefresh = ObservableBoolean()

    val isShowLoading = ObservableBoolean()

    fun create() {

    }

    fun onDestroy() {
        Logger.e("currentState:" + mLifecycle?.getCurrentState()?.name)
    }

    var mLifecycle: Lifecycle? = null

    fun setLifecycle(mLifecycle: Lifecycle) {
        this.mLifecycle = mLifecycle
        Logger.e("currentState:" + mLifecycle.currentState)
    }

    fun <T> bindLifecycle(): AutoDisposeConverter<T> {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(checkNotNull(mLifecycle)))
    }

}
