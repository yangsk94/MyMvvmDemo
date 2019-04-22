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
    override fun createViewModel(): BaseViewModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initGlobalParams() {

    }


    override val layoutId: Int
        get() = R.layout.fragment_me




    override val variableId: Int
        get() = 0

}