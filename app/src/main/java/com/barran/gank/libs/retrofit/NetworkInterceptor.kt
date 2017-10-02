package com.barran.gank.libs.retrofit

import com.barran.gank.utils.NetUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 统一处理网络及缓存
 *
 * Created by tanwei on 2017/9/27.
 */

class NetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response? {
        //To change body of created functions use File | Settings | File Templates.

        var request = chain?.request()
        if (!NetUtils.isNetworkAvailable) {
            //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
            request = request?.newBuilder()?.cacheControl(CacheControl.FORCE_CACHE)?.build()
        }

        var response = chain?.proceed(request!!)
        return if (NetUtils.isNetworkAvailable) {//有网络情况下，根据请求接口的设置，配置缓存。
            //这样在下次请求时，根据缓存决定是否真正发出请求。
            val cacheControl = request?.cacheControl().toString()
            //当然如果你想在有网络的情况下都直接走网络，那么只需要
            //将其超时时间这是为0即可:String cacheControl="Cache-Control:public,max-age=0"
            response?.newBuilder()?.header("Cache-Control", cacheControl)?.removeHeader("Pragma")?.build()
        } else {//无网络
            response?.newBuilder()?.header("Cache-Control", "public,only-if-cached,max-stale=360000")?.removeHeader("Pragma")?.build()

        }

    }
}