package com.medtap.network.library.commen

import com.medtap.network.library.ObserverCallBack.BaseCallBack
import com.utils.Logger
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


/**
 * subscribe的时候使用这个接口
 */
class Destiny<T>(baseCallBack: BaseCallBack<T>) : Observer<T> {
    var baseCallBack: BaseCallBack<T>? = baseCallBack

    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
        try {
            baseCallBack?.success(t)
        } catch (e: Exception) {
            if (Logger.DEBUG) {
                Logger.e("http_error", e.toString())
                e.printStackTrace()
                throw e
            }
        }
    }

    override fun onError(e: Throwable) {
        try {
            Logger.e(
                "\n" + "http_error:"
                        + "error_msg:"
                        + e.message
                        + ","
                        + "error_cause:"
                        + e.cause
            )
            baseCallBack?.failed(e.toString())
        } catch (e: Exception) {
            if (Logger.DEBUG) {
                Logger.e("http_error", e.toString())
                e.printStackTrace()
                throw e
            }
        }
    }

}