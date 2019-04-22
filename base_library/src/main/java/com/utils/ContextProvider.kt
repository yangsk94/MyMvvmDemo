package com.utils

import android.annotation.SuppressLint
import android.content.Context

/**
 * Created by yangsk on 2017/8/23.
 * <br></br><br></br>
 * Context辅助类，默认持有Application的Context
 */

object ContextProvider {

    var context: Context? = null
        private set

    fun holdContext(context: Context) {
        this.context = context
    }

}
