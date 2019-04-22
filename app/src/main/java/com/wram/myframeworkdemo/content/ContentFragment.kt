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
    override fun createViewModel(): ContentVM? = ContentVM(this@ContentFragment)

    var addOb: Observable<String>? = null

    @SuppressLint("CheckResult")
    override fun initGlobalParams() {
        addOb = RxBus.get().register("abc", String::class.java)
        addOb?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { s ->
                mBinding?.textView?.text = s
            }

    }


    override val layoutId: Int
        get() = R.layout.fragment_content

    override val variableId: Int
        get() = 0

    override fun onDestroy() {
        super.onDestroy()
        addOb?.let {
            RxBus.get().unregister(
                "abc", it
            )
        }
    }

}
