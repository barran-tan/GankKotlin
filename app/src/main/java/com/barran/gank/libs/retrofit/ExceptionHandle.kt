package com.barran.gank.libs.retrofit

import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.text.ParseException

/**
 * handle exception
 *
 * Created by tanwei on 2017/9/27.
 */

class ExceptionHandle {

    /**
     * 约定异常
     */
    internal object ERROR {
        /**
         * 未知错误
         */
        val UNKNOWN = 1000
        /**
         * 解析错误
         */
        val PARSE_ERROR = 1001
        /**
         * 网络错误
         */
        val NETWORD_ERROR = 1002
        /**
         * 协议出错
         */
        val HTTP_ERROR = 1003

        /**
         * 证书出错
         */
        val SSL_ERROR = 1005

        /**
         * 连接超时
         */
        val TIMEOUT_ERROR = 1006
    }

    class ResponseThrowable(throwable: Throwable, code: Int) : Exception(throwable) {
        var msg: String? = null
    }

    inner class ServerException : RuntimeException() {
        var code: Int = 0
        var msg: String? = null
    }

    companion object {
        private val UNAUTHORIZED = 401
        private val FORBIDDEN = 403
        private val NOT_FOUND = 404
        private val REQUEST_TIMEOUT = 408
        private val INTERNAL_SERVER_ERROR = 500
        private val BAD_GATEWAY = 502
        private val SERVICE_UNAVAILABLE = 503
        private val GATEWAY_TIMEOUT = 504

        fun handleException(e: Throwable): ResponseThrowable {
            val ex: ResponseThrowable
            if (e is HttpException) {
                ex = ResponseThrowable(e, ERROR.HTTP_ERROR)
                when (e.code()) {
                    UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE -> ex.msg = "网络错误"
                    else -> ex.msg = "网络错误"
                }
                return ex
            } else if (e is ServerException) {
                ex = ResponseThrowable(e, e.code)
                ex.msg = e.message
                return ex
            } else if (e is JsonParseException || e is JSONException
                    || e is ParseException) {
                ex = ResponseThrowable(e, ERROR.PARSE_ERROR)
                ex.msg = "解析错误"
                return ex
            } else if (e is ConnectException) {
                ex = ResponseThrowable(e, ERROR.NETWORD_ERROR)
                ex.msg = "连接失败"
                return ex
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                ex = ResponseThrowable(e, ERROR.SSL_ERROR)
                ex.msg = "证书验证失败"
                return ex
            } else if (e is java.net.SocketTimeoutException) {
                ex = ResponseThrowable(e, ERROR.TIMEOUT_ERROR)
                ex.msg = "连接超时"
                return ex
            } else {
                ex = ResponseThrowable(e, ERROR.UNKNOWN)
                ex.msg = "未知错误"
                return ex
            }
        }
    }
}
