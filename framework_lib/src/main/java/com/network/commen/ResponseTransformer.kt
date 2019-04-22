package com.medtap.network.library.commen

import com.network.entity.BaseEntity
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function

/**
 * 统一处理error，剥离data层
 */
class ResponseTransformer {


    class ResponseFunction<T> : Function<BaseEntity<T>, ObservableSource<out T>> {
        override fun apply(t: BaseEntity<T>): ObservableSource<out T> {
            val code = t.code
            val msg = t.message
            val content = t.data
            return if (code == 200) {
                Observable.just(content!!)
            } else {
                Observable.error(code?.let { msg?.let { it1 -> ApiException(it, it1) } })
            }
        }
    }

    class ErrorResumeFunction<T> : Function<Throwable, ObservableSource<BaseEntity<out T>>>  {

        override fun apply(e: Throwable): ObservableSource<BaseEntity<out T>> {

            return Observable.error(CustomException.handleException(e))
        }
    }

}