package com.wram.myframeworkdemo.home

import android.arch.lifecycle.MutableLiveData
import com.medtap.network.library.IOTransFormHelper.RxStreamHelper
import com.medtap.network.library.ObserverCallBack.BaseCallBack
import com.medtap.network.library.api.ApiClient
import com.medtap.network.library.commen.Destiny
import com.navigation.BaseViewModel
import com.navigation.PageFragment
import com.network.bean.AreaBean
import com.network.bean.LoginReqData
import com.network.bean.UserInfo
import com.widgets.ToastCompat

/**
 * @author
 * @class describe  [.]
 * @time 2019/4/19 下午4:14
 */
class ViewModel(activity: PageFragment, private val navigator: Navigator) : BaseViewModel(activity) {

    var data = MutableLiveData<String>()

    init {
        data.value = "ing"
    }

    fun login() {

        ApiClient.instance.getApiService()
            .login(LoginReqData("18100000001", "123456", 20))
            .compose(bindToLifecycleFragment())
            .compose(RxStreamHelper().mainThread())
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

    fun getData() {
        ApiClient.instance.getApiService().getAreaList()
            .compose(bindToLifecycleFragment())
            .compose(RxStreamHelper().mainThread())
            .subscribe(Destiny(object : BaseCallBack<AreaBean> {
                override fun success(any: AreaBean) {
                    context?.let { ToastCompat.showToast(it, any.toString()) }
                }

                override fun failed(e: String) {
                    context?.let { ToastCompat.showToast(it, e) }
                }

            }))
    }


    interface Navigator {
        fun success(info: UserInfo?)

        fun failed(msg: String)
    }

}
