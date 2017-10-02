package com.barran.gank.libs.retrofit

/**
 * base response of http
 *
 * Created by tanwei on 2017/9/27.
 */

class BaseResponse<T> {
    var code: Int = 0
    var msg: String? = null
    var response: T? = null

    val isOk: Boolean
        get() = code == 0
}
