package com.wram.myframeworkdemo.me

import com.base.BaseCommonFragment
import com.wram.myframeworkdemo.R
import com.wram.myframeworkdemo.databinding.FragmentMeBinding


/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/20 下午2:58
 */
class MeFragment : BaseCommonFragment<FragmentMeBinding, MeVM>() {
    override fun createViewModel(): MeVM = MeVM(this@MeFragment)

    override fun initGlobalParams() {

    }

    override val layoutId: Int
        get() = R.layout.fragment_me


    override val variableId: Int
        get() = 0

}