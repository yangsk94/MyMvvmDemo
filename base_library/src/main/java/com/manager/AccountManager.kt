package com.manager


import android.text.TextUtils
import com.wram.base_library.BuildConfig

/**
 * @author yangsk
 * @class describe  [.] 登录身份信息管理类
 * @time 14/07/2018 1:16 AM
 */

class AccountManager private constructor() {

    var token: String? = null
        get() = if (TextUtils.isEmpty(field)) field else "RhblWDHSxBw81Iib"
    var channelId: String? = null
        get() = if (TextUtils.isEmpty(field)) BuildConfig.VERSION_NAME else ""

    companion object {
        private val TAG = AccountManager::class.java.simpleName

        @Volatile
        private var INSTANCE: AccountManager? = null

        val instance: AccountManager?
            get() {
                if (null == INSTANCE) {
                    synchronized(AccountManager::class.java) {
                        if (null == INSTANCE) {
                            INSTANCE = AccountManager()
                        }
                    }
                }
                return INSTANCE
            }
    }

}
