package com.barran.gank.libs.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 顶级拦截器，设置统一header等
 *
 * Created by tanwei on 2017/9/27.
 */

internal class BaseInterceptor(private val headers: Map<String, String>?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val builder = chain.request()
                .newBuilder()
        if (headers != null && headers.isNotEmpty()) {
            val keys = headers.keys
            keys.filter { headers[it] != null }.forEach { key ->
                builder.addHeader(key, headers[key] ?: error("")).build()
            }
        }
        return chain.proceed(builder.build())

    }
}
