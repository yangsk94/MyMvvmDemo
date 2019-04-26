package com.wram.myframeworkdemo.home

import android.content.Context
import android.databinding.ObservableField
import com.medtap.network.library.IOTransFormHelper.RxStreamManager
import com.medtap.network.library.ObserverCallBack.BaseCallBack
import com.medtap.network.library.api.ApiClient
import com.medtap.network.library.commen.Destiny
import com.navigation.BaseViewModel
import com.network.bean.AreaBean
import com.network.bean.LoginReqData
import com.network.bean.UserInfo
import com.widgets.ToastCompat
import java.util.*

/**
 * @author
 * @class describe  [.]
 * @time 2019/4/19 下午4:14
 */
class ViewModel(context: Context, private val navigator: Navigator) : BaseViewModel(context) {

    var data = ObservableField<String>()
    var svgSource = ObservableField<String>()

    init {
        data.set("ing")
    }

    fun login() {

        ApiClient.instance.getApiService()
            .login(LoginReqData("18100000001", "123456", 20))
            .compose(RxStreamManager().mainThread())
            .`as`(bindLifecycle())
            .subscribe(Destiny(object : BaseCallBack<UserInfo> {
                override fun success(any: UserInfo) {
                    data.set("success")

                    navigator.success(any)
                }

                override fun failed(e: String) {
                    context?.let { ToastCompat.showToast(it, "failed") }
                    data.set("failed")
                    navigator.failed(e)
                }

            }))
    }

    var count: Int = 0

    fun getData() {
        ApiClient.instance.getApiService().getAreaList()
            .compose(RxStreamManager().mainThread())
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

    val samples = ArrayList<String>()


    fun randomSample(): String {
        if (samples.size == 0) {
            samples.add("angel.svga")
            samples.add("alarm.svga")
            samples.add("EmptyState.svga")
            samples.add("heartbeat.svga")
            samples.add("posche.svga")
            samples.add("rose_1.5.0.svga")
            samples.add("rose_2.0.0.svga")
            samples.add("test.svga")
            samples.add("test2.svga")
        }
        return samples.get(Math.floor(Math.random() * samples.size).toInt())
    }


    interface Navigator {

        fun success(info: UserInfo?)

        fun failed(msg: String)
    }

}
