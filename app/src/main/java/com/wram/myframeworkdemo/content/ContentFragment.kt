package com.wram.myframeworkdemo.content

import android.annotation.SuppressLint
import com.base.BaseCommonFragment
import com.event.RxBus
import com.wram.myframeworkdemo.R
import com.wram.myframeworkdemo.databinding.FragmentContentBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @author ysk
 * @class describe  [.]
 * @time 2019/4/20 下午2:59
 */
class ContentFragment : BaseCommonFragment<FragmentContentBinding, ContentVM>() {


    override fun createViewModel(): ContentVM? = context?.let { ContentVM(it) }

    var addOb: Observable<String>? = null

    @SuppressLint("CheckResult")
    override fun initGlobalParams() {
        addOb = RxBus.get().register("abc")
        addOb?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { s ->
                mBinding?.textView?.text = s
            }

        mBinding?.recyclerView?.setData()

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_content
    }

    override fun getVariableId(): Int {
        return 0
    }

    override fun onDestroy() {
        super.onDestroy()
        addOb?.let {
            RxBus.get().unregister(
                "abc", it
            )
        }
    }

}
