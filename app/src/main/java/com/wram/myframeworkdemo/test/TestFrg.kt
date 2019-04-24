package com.wram.myframeworkdemo.test

import com.base.BaseCommonFragment
import com.event.RxBus
import com.network.bean.UserInfo
import com.wram.myframeworkdemo.R
import com.wram.myframeworkdemo.databinding.FragmentTestBinding
import com.wram.myframeworkdemo.home.ViewModel


/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/20 下午3:31
 */
class TestFrg : BaseCommonFragment<FragmentTestBinding, ViewModel>(), ViewModel.Navigator {

    override fun success(info: UserInfo?) {

    }

    override fun failed(msg: String) {
        showToast(msg)
    }

    override fun createViewModel(): ViewModel? = context?.let { ViewModel(it, this) }

    override fun initGlobalParams() {
        RxBus.get().post("abc", "RxBus")
        for (i in 0..199) {
            mViewModel?.getData()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_test
    }

    override fun getVariableId(): Int {
        return 0
    }


}