package com.barran.gank.app

import android.app.Application
import android.content.Context
import com.barran.gank.libs.retrofit.RetrofitClient

/**
 * app entry
 *
 * Created by tanwei on 2017/9/26.
 */

class App : Application() {

    companion object {
        lateinit var appContext: Context
    }


    override fun onCreate() {
        super.onCreate()

        appContext = this

        initLibs()
    }

    private fun initLibs() {
        RetrofitClient.instance.init()
    }

}
