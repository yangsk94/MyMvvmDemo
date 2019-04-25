package com.wram.myframeworkdemo.home

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.medtap.network.library.IOTransFormHelper.RxStreamHelper
import com.medtap.network.library.ObserverCallBack.BaseCallBack
import com.medtap.network.library.api.ApiClient
import com.medtap.network.library.commen.Destiny
import com.navigation.BaseViewModel
import com.network.bean.AreaBean
import com.network.bean.LoginReqData
import com.network.bean.UserInfo
import com.widgets.ToastCompat

/**
 * @author
 * @class describe  [.]
 * @time 2019/4/19 下午4:14
 */
class ViewModel(context: Context, private val navigator: Navigator) : BaseViewModel(context) {

    var data = MutableLiveData<String>()

    init {
        data.value = "ing"
    }

    fun login() {

        ApiClient.instance.getApiService()
            .login(LoginReqData("18100000001", "123456", 20))
            .compose(RxStreamHelper().mainThread(navigator))
            .`as`(bindLifecycle())
            .subscribe(Destiny(object : BaseCallBack<UserInfo> {
                override fun success(any: UserInfo) {
                    data.postValue("success")

                    navigator.success(any)
                }

                override fun failed(e: String) {
                    context?.let { ToastCompat.showToast(it, "failed") }
                    data.postValue("failed")
                    navigator.failed(e)
                }

            }))
    }

    var count: Int = 0

    fun getData() {
         ApiClient.instance.getApiService().getAreaList()
             .compose(RxStreamHelper().mainThread(navigator))
             .`as`(bindLifecycle())
             .subscribe(Destiny(object : BaseCallBack<AreaBean> {
                 override fun success(any: AreaBean) {
                     count++
                     context?.let { ToastCompat.showToast(it, any.toString() + count) }
                 }

                 override fun failed(e: String) {
                     count++

                     navigator.failed(e + count)
                 }

             }))
//模拟内存泄露
        /*Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            //AutoDispose的使用就是这句
            .`as`(bindLifecycle())
            .subscribe(object : Observer<Long> {
                override fun onNext(t: Long) {
                    Log.i("接收数据,当前线程" + Thread.currentThread().name, t.toString())

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })*/


    }


    interface Navigator {

        fun success(info: UserInfo?)

        fun failed(msg: String)
    }

}
