package com.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import com.navigation.BaseActivity
import com.navigation.BaseViewModel

/**
 * @author ysk
 * @class describe  [.]
 * @time 2019/4/20 下午12:42
 */
abstract class BaseCommonActivity<B : ViewDataBinding, VM : BaseViewModel> : BaseActivity() {

    var binding: B? = null
        protected set
    var viewModel: VM? = null
        protected set

    protected abstract val layoutId: Int

    protected abstract val variableId: Int

    public override fun onCreate(savedInstanceState: Bundle?) {
        getIntentParams()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        if (variableId != 0) {
            binding?.setVariable(variableId, viewModel)
            binding?.executePendingBindings()
        }
        viewModel?.create()
        initGlobalParams()
    }


    protected abstract fun initGlobalParams()


    public override fun onDestroy() {
        super.onDestroy()
        viewModel?.onDestroy()
    }

    protected abstract fun createViewModel(): VM

    fun initStateBar() {

    }

    fun getIntentParams() {

    }


}
