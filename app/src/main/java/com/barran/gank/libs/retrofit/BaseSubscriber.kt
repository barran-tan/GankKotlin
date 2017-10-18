package com.barran.gank.libs.retrofit

import android.content.Context
import android.util.Log
import android.widget.Toast

import com.barran.gank.utils.NetUtils
import com.barran.gank.utils.toast

import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody

/**
 * base [Obverser][io.reactivex.Observer]
 *
 * Created by tanwei on 2017/9/27.
 */

abstract class BaseSubscriber : Observer<ResponseBody> {

    private val TAG = "baseSub"

    private val context: Context? = null

    override fun onComplete() {
        Log.i(TAG, "onCompleted")
        "http is Complete".toast()
        // todo some common as dismiss loadding

    }

    override fun onSubscribe(@NonNull d: Disposable) {

        Log.i(TAG, "onSubscribe")

        // must to call onCompleted
        if (!NetUtils.isNetworkAvailable) {
            "无网络，读取缓存数据".toast()
            onComplete()
        }
    }

    override fun onError(@NonNull e: Throwable) {

        Log.w(TAG, e.message)

        if (e is ExceptionHandle.ResponseThrowable) {
            onError(e)
        } else {
            onError(ExceptionHandle.ResponseThrowable(e,
                    ExceptionHandle.ERROR.UNKNOWN))
        }
    }

    abstract fun onError(e: ExceptionHandle.ResponseThrowable)
}
