package com.barran.gank.api;

import com.barran.gank.api.beans.DailyDataResponse;
import com.barran.gank.api.beans.DataResponse;
import com.barran.gank.api.beans.HistoryDates;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 定义通用的api访问接口，避免重复定义
 *
 * Created by tanwei on 2017/9/27.
 */

public interface APIService {
    
    public static final String Base_URL = "http://gank.io/";
    
    /**
     * 获取发过干货日期接口:
     * 
     * http://gank.io/api/day/history
     */
    @GET("api/day/history")
    Observable<HistoryDates> getHistoryDates();

    /**
     * 获取特定日期网站数据:http://gank.io/api/day/年/月/日
     * http://gank.io/api/day/2015/08/06
     */
    @GET("{url}")
    Observable<DailyDataResponse> getDailyData(@Path("url") String url);

    /**
     * 获取分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     *
     * <p>注意页数从1开始，传入0页数第1页</p>
     *
     * http://gank.io/api/data/Android/10/1
     *
     */
    @GET("{url}")
    Observable<DataResponse> getDataByType(@Path("url") String url);
}
