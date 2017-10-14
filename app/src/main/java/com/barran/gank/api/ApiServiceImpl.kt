package com.barran.gank.api

import com.barran.gank.libs.retrofit.RetrofitClient
import com.barran.gank.api.beans.DailyDataResponse
import com.barran.gank.api.beans.DatasResponse
import com.barran.gank.api.beans.HistoryDates
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 具体接口
 *
 * Created by tanwei on 2017/9/27.
 */
object ApiServiceImpl {

    /**
     * 获取发过干货日期接口:

     * http://gank.io/api/day/history
     */
    fun getHistoryDates(subscriber: Observer<HistoryDates>) {

        RetrofitClient.instance.apiService.historyDates
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }

    /**
     * 获取特定日期网站数据:http://gank.io/api/day/年/月/日
     * http://gank.io/api/day/2015/08/06
     */
    fun getDailyData(year: Int, month: Int, day: Int, subscriber: Observer<DailyDataResponse>) {

        RetrofitClient.instance.apiService.getDailyData("api/day/$year/$month/$day")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }

    /**
     * 获取分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * http://gank.io/api/data/Android/10/1
     *
     */
    fun getDataByType(type: String, pageCount: Int, pageNum: Int, subscriber: Observer<DatasResponse>) {

        RetrofitClient.instance.apiService.getDataByType("api/data/$type/$pageCount/$pageNum")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }
}