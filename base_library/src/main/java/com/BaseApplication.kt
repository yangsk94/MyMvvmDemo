package com

import android.app.Application
import android.content.Context
import com.codemonkeylabs.fpslibrary.TinyDancer
import com.network.NetworkMonitor
import com.squareup.leakcanary.LeakCanary
import com.utils.ContextProvider
import com.utils.Logger
import com.utils.SystemUtil
import com.wram.base_library.BuildConfig

/**
 * @author yangsk
 * @class describe  [.]
 * @time 2019/4/16 下午4:18
 */
class BaseApplication : Application() {
    var TAG = javaClass.simpleName

    var isDebuggable: Boolean = false

    val channelValue: String? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        //        MultiDex.install(this);

        isDebuggable = BuildConfig.DEBUG
    }

    override fun onCreate() {
        super.onCreate()
        val start = System.currentTimeMillis()
        if (SystemUtil.isMainProcess(this)) {
            context = this
            initDefaultPlatform()
            val end = System.currentTimeMillis()
            Logger.e(TAG, "主进程检测消耗时间：" + (end - start) + "毫秒")
        }
    }

    /**
     * 默认初始化操作
     */
    fun initDefaultPlatform() {

        // Context辅助类
        ContextProvider.holdContext(this)

        // Crash及日志模块
        Logger.debugEnabled(isDebuggable)

        // 网络模块
        NetworkMonitor.instance?.startMonitor(context)

        // 性能检测模块
        if (!LeakCanary.isInAnalyzerProcess(this) && isDebuggable) {
            TinyDancer.create().show(this)
            LeakCanary.install(this)
        }

    }

    companion object {

        var context: BaseApplication? = null
            private set
    }
}