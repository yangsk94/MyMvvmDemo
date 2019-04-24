package com.medtap.network.library.api

import com.BaseApplication
import com.manager.AccountManager
import com.network.helper.HttpLogger
import com.utils.DeviceUtil
import com.utils.FileUtil
import com.utils.SignUtil
import com.wram.base_library.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

class InternalOkHttpClient {
    companion object {
        private val DEFAULT_TIME_OUT_SECONDS = 30

        fun getOkhttpClient(): OkHttpClient? {
            var okHttpClient: OkHttpClient? = null

            if (okHttpClient == null) {
                val cacheFile = BaseApplication.context?.let { FileUtil.getPublicDir(it) }
                val cache = Cache(cacheFile, (1024 * 1024 * 50).toLong())
                okHttpClient = OkHttpClient.Builder()
                    .cache(cache)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(DEFAULT_TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
                    .build()

                if (BuildConfig.DEBUG) {//printf logs while  debug
                    okHttpClient = okHttpClient
                        ?.newBuilder()
                        ?.addInterceptor(AppInterceptor())
                        ?.addInterceptor(HttpLoggingInterceptor(HttpLogger()).setLevel(HttpLoggingInterceptor.Level.BASIC))
                        ?.build()

                }
            }
            return okHttpClient
        }

        private class AppInterceptor : Interceptor {

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val builder = request.newBuilder()

                builder.addHeader("Authorization", "token " + AccountManager.instance!!.token)

                if (request.method() != "GET") {
                    builder.addHeader("Content-Type", "application/json")
                }
                builder.addHeader("charset", "utf-8")
                builder.addHeader("connection", "close")
                builder.addHeader("User-Agent", "SKAPP-" + DeviceUtil.appVersion + " android " + DeviceUtil.sdkVersion)
                builder.addHeader("channel", AccountManager.instance!!.channelId)
                try {
                    val nonce = SignUtil.nonce
                    val secret = SignUtil.encryptHMAC(nonce)
                    builder.addHeader("X-Signature", "$nonce:$secret".trim { it <= ' ' })
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return chain.proceed(builder.build())
            }
        }
    }
}