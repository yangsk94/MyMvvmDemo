package com.medtap.network.library.IOTransFormHelper

import com.medtap.network.library.commen.ApiException
import com.medtap.network.library.commen.CustomException
import com.medtap.network.library.errorcode.Error
import com.network.entity.BaseEntity
import com.wram.myframeworkdemo.home.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers


/**
 * 统一处理，线程调度
 */
class RxStreamHelper() {

    fun <T> mainThread(navigator: ViewModel.Navigator): ObservableTransformer<BaseEntity<T>, T> {
        return ObservableTransformer { upstream ->
            upstream
                .compose(navigator.bindLifecycle())

                .subscribeOn(Schedulers.io())
                //出错统一处理
                .onErrorResumeNext(Function { throwable -> Observable.error(CustomException.handleException(throwable)) })

                //解析data层，剔除 code /msg
                .flatMap { tBaseModel ->
                    if (tBaseModel.code == Error.SUCCESS) Observable.just(tBaseModel.data)
                    else Observable.error(tBaseModel.message?.let { ApiException(tBaseModel.code!!, it) })
                }
                .observeOn(AndroidSchedulers.mainThread())
        }

    }
}
