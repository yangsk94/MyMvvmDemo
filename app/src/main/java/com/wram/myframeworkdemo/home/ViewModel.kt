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
import io.reactivex.ObservableTransformer

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
            .subscribe(Destiny(object : BaseCallBack<UserInfo> {
                override fun success(any: UserInfo) {
                    data.postValue("success")

                    navigator.success(any)
                }

                override fun failed(e: String) {
                    data.postValue("failed")
                    navigator.failed(e)
                }

            }))
    }

    var count: Int = 0

    fun getData() {
        ApiClient.instance.getApiService().getAreaList()
            .compose(RxStreamHelper().mainThread(navigator))
            .subscribe(Destiny(object : BaseCallBack<AreaBean> {
                override fun success(any: AreaBean) {
                    count++
                    context?.let { ToastCompat.showToast(it, any.toString() + count) }
                }

                override fun failed(e: String) {
                    count++
                    context?.let { ToastCompat.showToast(it, e + count) }
                }

            }))
    }


    interface Navigator {
        fun <T> bindLifecycle(): ObservableTransformer<T, T>

        fun success(info: UserInfo?)

        fun failed(msg: String)
    }

}
