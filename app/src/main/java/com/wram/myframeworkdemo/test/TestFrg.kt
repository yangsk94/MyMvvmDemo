package com.wram.myframeworkdemo.test

import com.base.BaseCommonFragment
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

    override fun createViewModel(): ViewModel? = ViewModel(this@TestFrg, this)

    override fun initGlobalParams() {
        RxBus.get().post("abc", "RxBus")
        for (i in 0..199) {
            mViewModel?.getData()
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_test


    override val variableId: Int
        get() = 0


}