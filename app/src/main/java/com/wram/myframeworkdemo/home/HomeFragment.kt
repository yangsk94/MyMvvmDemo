package com.wram.myframeworkdemo.home

import com.base.BaseCommonFragment
import com.navigation.Navigation
import com.network.bean.UserInfo
import com.wram.myframeworkdemo.BR
import com.wram.myframeworkdemo.R
import com.wram.myframeworkdemo.databinding.FragmentHomeBinding
import com.wram.myframeworkdemo.test.TestFrg

/**
 * @author ysk
 * @class describe  [.]
 * @time 2019/4/20 下午2:58
 */
class HomeFragment : BaseCommonFragment<FragmentHomeBinding, ViewModel>(), ViewModel.Navigator {
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getVariableId(): Int {
        return BR.vm
    }

    override fun success(info: UserInfo?) {

    }

    override fun failed(msg: String) {

    }

    override fun initGlobalParams() {
        mBinding?.textView?.setOnClickListener {
            Navigation.navigationOpen(context, TestFrg::class.java.name)
        }

        mViewModel?.login()

    }

    override fun createViewModel() = context?.let { ViewModel(it, this) }


}
