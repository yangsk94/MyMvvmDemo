package com.wram.myframeworkdemo.me

import com.base.BaseCommonFragment
import com.wram.myframeworkdemo.BR
import com.wram.myframeworkdemo.R
import com.wram.myframeworkdemo.common.ErrorLayout
import com.wram.myframeworkdemo.databinding.FragmentMeBinding
import kotlinx.android.synthetic.main.fragment_me.*


/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/20 下午2:58
 */
class MeFragment : BaseCommonFragment<FragmentMeBinding, MeVM>(), ErrorLayout.ClickRefreshListener {
    override fun onClickRefreshListener() {
        mViewModel.isShowRefresh.set(false)
        mViewModel.isShowLoading.set(true)
    }

    override fun createViewModel() = context?.let { MeVM(it) }

    override fun initGlobalParams() {
        mBinding.textView.setOnClickListener {

            mViewModel.isShowRefresh.set(true)
            mViewModel.isShowLoading.set(false)
        }

        errorLayout.setViewModel(mViewModel)
        errorLayout.setClickRefreshListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }

    override fun getVariableId(): Int {
        return BR.mvm
    }

}