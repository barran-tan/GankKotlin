package com.barran.gank.libs.retrofit

import com.barran.gank.app.App
import com.barran.gank.api.APIService
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 主要负责创建具体Retrofit，基础配置
 *
 * Created by tanwei on 2017/9/27.
 */

class RetrofitClient private constructor() {

    lateinit var retrofit: Retrofit

    lateinit var apiService : APIService

    companion object {
        val DEFAULT_TIMEOUT = 20L
        val instance = RetrofitClient()
    }

    fun init() {
        val cacheDirectory = File(App.appContext.cacheDir
                .absolutePath, "HttpCache")
        val cache = Cache(cacheDirectory, 20 * 1024 * 1024)

        val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(NetworkInterceptor())
                .addInterceptor(BaseInterceptor(null))
                .addInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
                .build()

        // 处理特殊的时间格式
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create()

        retrofit = Retrofit.Builder().client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl(APIService.Base_URL)
                .build()

        apiService = retrofit.create(APIService::class.java)
    }

}
