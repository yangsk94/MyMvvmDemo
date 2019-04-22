package com.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.util.*

/**
 * Created by yangsk on 2017/5/21.
 */

class NetworkMonitor private constructor() {

    private var mContext: Context? = null
    private var mNetworkReceiver: NetworkStatusChangeReceiver? = null

    private var mCurrentNetStatus: Int = 0
    private var mNetObservers: MutableList<NetStatusObserver>? = null

    /**
     * 无网络
     *
     */
    val isNetworkAvailable: Boolean
        get() {
            refreshNetworkStatus()
            return mCurrentNetStatus != NETWORK_STATUS_UNAVAILABLE
        }

    /**
     * WiFi
     *
     */
    val isWifiAvailable: Boolean
        get() {
            refreshNetworkStatus()
            return mCurrentNetStatus == NETWORK_STATUS_WIFI
        }

    /**
     * 手机
     *
     */
    val isMobileAvailable: Boolean
        get() {
            refreshNetworkStatus()
            return mCurrentNetStatus == NETWORK_STATUS_MOBILE
        }

    fun startMonitor(context: Context?) {
        if (null == context) {
            throw NullPointerException("context is null when monitor network !")
        }
        mContext = context.applicationContext
        refreshNetworkStatus()
        if (null == mNetworkReceiver) {
            mNetworkReceiver = NetworkStatusChangeReceiver()
        }
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        mContext?.registerReceiver(mNetworkReceiver, filter)
    }

    fun stopMonitor() {
        if (null != mContext && null != mNetworkReceiver) {
            mContext?.unregisterReceiver(mNetworkReceiver)
        }
        if (null != mNetworkReceiver) {
            mNetworkReceiver = null
        }
        if (null != mNetObservers) {
            mNetObservers?.clear()
            mNetObservers = null
        }
    }

    private fun refreshNetworkStatus() {
        val connectivityManager = mContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (null == networkInfo || !networkInfo.isAvailable) {
            mCurrentNetStatus = NETWORK_STATUS_UNAVAILABLE
            return
        }
        val type = networkInfo.type
        if (ConnectivityManager.TYPE_WIFI == type) {
            mCurrentNetStatus = NETWORK_STATUS_WIFI
            return
        }
        if (type == ConnectivityManager.TYPE_MOBILE
            || type == ConnectivityManager.TYPE_MOBILE_MMS
            || type == ConnectivityManager.TYPE_MOBILE_DUN
            || type == ConnectivityManager.TYPE_MOBILE_HIPRI
            || type == ConnectivityManager.TYPE_MOBILE_SUPL
            || type == ConnectivityManager.TYPE_WIMAX
        ) {
            mCurrentNetStatus = NETWORK_STATUS_MOBILE
            return
        }
        mCurrentNetStatus = NETWORK_STATUS_UNAVAILABLE
    }

    fun addNetStatusObserver(observer: NetStatusObserver) {
        if (null == mNetObservers) {
            mNetObservers = ArrayList()
        }
        mNetObservers?.add(observer)
    }

    fun removeNetStatusObserver(observer: NetStatusObserver) {
        if (null != mNetObservers && mNetObservers!!.contains(observer)) {
            mNetObservers?.remove(observer)
        }
    }

    private inner class NetworkStatusChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (null != mNetObservers && ConnectivityManager.CONNECTIVITY_ACTION == action) {
                val lastStatus = mCurrentNetStatus
                refreshNetworkStatus()
                if (lastStatus != mCurrentNetStatus) {
                    notifyNetworkStatusChanged()
                }
            }
        }
    }

    private fun notifyNetworkStatusChanged() {
        val available = mCurrentNetStatus == NETWORK_STATUS_MOBILE || mCurrentNetStatus == NETWORK_STATUS_WIFI
        for (observer in this.mNetObservers!!) {
            observer.observer(available, mCurrentNetStatus)
        }
    }

    interface NetStatusObserver {
        fun observer(available: Boolean, currentNetStatus: Int)
    }

    companion object {

        private val NETWORK_STATUS_UNAVAILABLE = 0
        private val NETWORK_STATUS_WIFI = 1
        private val NETWORK_STATUS_MOBILE = 2
        private var sNetworkMonitor: NetworkMonitor? = null

        val instance: NetworkMonitor?
            get() {
                if (null == sNetworkMonitor) {
                    synchronized(NetworkMonitor::class.java) {
                        if (null == sNetworkMonitor) {
                            sNetworkMonitor = NetworkMonitor()
                        }
                    }
                }
                return sNetworkMonitor
            }
    }
}