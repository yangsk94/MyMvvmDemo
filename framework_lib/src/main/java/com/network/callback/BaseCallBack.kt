package com.medtap.network.library.ObserverCallBack

interface BaseCallBack<T> {
    fun success(any: T)
    fun failed(e: String)
}