package com.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.navigation.BaseViewModel
import com.navigation.PageFragment

/**
 * @author ysk
 * @class describe  [.]
 * @time 2019/4/20 下午12:42
 */
abstract class BaseCommonFragment<B : ViewDataBinding, VM : BaseViewModel> : PageFragment() {

    var mBinding: B? = null

    var mViewModel: VM? = null

    protected abstract val layoutId: Int

    protected abstract val variableId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentParams()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId, container, false)
        mBinding = DataBindingUtil.bind(view)
        mViewModel = if (mViewModel == null) createViewModel() else mViewModel
        if (mViewModel == null || variableId == 0) return view
        mBinding?.lifecycleOwner = this
        mBinding?.setVariable(variableId, mViewModel)
        mBinding?.executePendingBindings()
        return view
    }

    protected abstract fun initGlobalParams()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mViewModel != null) mViewModel?.create()
        initGlobalParams()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mViewModel != null) {
            mViewModel?.onDestroy()
            mViewModel = null
        }
    }

    protected abstract fun createViewModel(): VM?

    fun initStateBar() {

    }

    fun getIntentParams() {

    }


}
