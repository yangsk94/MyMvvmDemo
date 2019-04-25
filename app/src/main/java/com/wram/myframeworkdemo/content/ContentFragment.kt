package com.wram.myframeworkdemo.content

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import com.base.BaseCommonFragment
import com.common.RefreshRecyclerView
import com.event.RxBus
import com.utils.TaskEngine
import com.wram.myframeworkdemo.R
import com.wram.myframeworkdemo.databinding.FragmentContentBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_content.*
import kotlin.concurrent.timerTask

/**
 * @author ysk
 * @class describe  [.]
 * @time 2019/4/20 下午2:59
 */
class ContentFragment : BaseCommonFragment<FragmentContentBinding, ContentVM>(), RefreshRecyclerView.OnScrollListener {
    override fun onRefresh(curPage: Int) {

        TaskEngine.instance?.schedule(timerTask {
            rv?.refreshComplete()
        }, 3000)
    }

    override fun onMore() {

    }


    override fun createViewModel(): ContentVM? = context?.let { ContentVM(it) }

    var addOb: Observable<String>? = null

    @SuppressLint("CheckResult")
    override fun initGlobalParams() {
        addOb = RxBus.get().register("abc")
        addOb?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { s ->
                mBinding?.textView?.text = s
            }

        rv.let {
            it.addOnScrollListener(this)
            it.setPullRefreshEnabled(true)

            it.initLayoutManager(LinearLayoutManager(context))
            it.register(ContentBean::class.java, ContentBinder())
        }
    }

    override fun lazyLoad() {
        for (i in 0..30) {
            rv?.list?.add(ContentBean("测试数据$i"))
        }
        rv?.notifyDataSetChanged();
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
            RxBus.get().unregister("abc", it)
        }
    }

}
