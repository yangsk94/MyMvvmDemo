package com.wram.myframeworkdemo.content

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.navigation.BaseViewModel

/**
 * @author ysk
 * @class describe  [.]
 * @time 2019/4/22 上午10:35
 */
class ContentVM(context: Context) : BaseViewModel(context) {

    var data = MutableLiveData<String>()



}
