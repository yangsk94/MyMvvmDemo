package com.wram.myframeworkdemo.me

import com.base.BaseCommonFragment
import com.navigation.BaseViewModel
import com.wram.myframeworkdemo.R
import com.wram.myframeworkdemo.databinding.FragmentMeBinding


/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/20 下午2:58
 */
class MeFragment : BaseCommonFragment<FragmentMeBinding, BaseViewModel>() {
    override fun createViewModel() = null

    override fun initGlobalParams() {

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }

    override fun getVariableId(): Int {
        return 0
    }

}