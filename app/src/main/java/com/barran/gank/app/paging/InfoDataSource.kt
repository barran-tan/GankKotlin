package com.barran.gank.app.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.barran.gank.api.ApiServiceImpl
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.api.beans.DataResponse
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 分页加载信息
 *
 * create by tanwei@bigo.sg
 * on 2019/12/12
 */
class InfoDataSource(private val type: String) : PageKeyedDataSource<Int, DataInfo>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, DataInfo>) {

        Log.v(PagingAdapter.TAG, "loadInitial type=$type size=${params.requestedLoadSize} placeholdersEnabled=${params.placeholdersEnabled}")

        ApiServiceImpl.getDataByType(type, params.requestedLoadSize, 1, object : Observer<DataResponse> {

            override fun onComplete() {
                Log.v(PagingAdapter.TAG, "loadInitial onComplete")
            }

            override fun onError(e: Throwable) {
                Log.v(PagingAdapter.TAG, "loadInitial onError")
            }

            override fun onNext(t: DataResponse) {
                Log.v(PagingAdapter.TAG, "loadInitial onNext size= ${t.results.size}")

                if (t.results.isEmpty()) {
                    callback.onResult(t.results, null, null)
                } else {
                    callback.onResult(t.results, null, 2)
                }
            }

            override fun onSubscribe(d: Disposable) {
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataInfo>) {

        Log.v(PagingAdapter.TAG, "loadAfter type=$type size=${params.requestedLoadSize} page=${params.key}")

        ApiServiceImpl.getDataByType(type, params.requestedLoadSize, params.key, object : Observer<DataResponse> {

            override fun onComplete() {
                Log.v(PagingAdapter.TAG, "loadAfter onComplete")
            }

            override fun onError(e: Throwable) {
                Log.v(PagingAdapter.TAG, "loadAfter onError")
            }

            override fun onNext(t: DataResponse) {
                Log.v(PagingAdapter.TAG, "loadAfter onNext size= ${t.results.size}")

                if (t.results.isEmpty()) {
                    callback.onResult(t.results, null)
                    Log.i(PagingAdapter.TAG, "loadAfter no more and set next key null")
                } else {
                    val next = params.key + 1
                    callback.onResult(t.results, next)
                    Log.i(PagingAdapter.TAG, "loadAfter has more next key $next")
                }
            }

            override fun onSubscribe(d: Disposable) {
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataInfo>) {

        Log.v(PagingAdapter.TAG, "loadBefore type=$type size=${params.requestedLoadSize} page=${params.key}")

        ApiServiceImpl.getDataByType(type, params.requestedLoadSize, params.key, object : Observer<DataResponse> {

            override fun onComplete() {
                Log.v(PagingAdapter.TAG, "loadBefore onComplete")
            }

            override fun onError(e: Throwable) {
                Log.v(PagingAdapter.TAG, "loadBefore onError")
            }

            override fun onNext(t: DataResponse) {
                Log.v(PagingAdapter.TAG, "loadBefore onNext")

                callback.onResult(t.results, params.key - 1)
            }

            override fun onSubscribe(d: Disposable) {
            }
        })
    }
}