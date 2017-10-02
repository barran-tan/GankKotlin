package com.barran.gank.service;

import com.barran.gank.service.beans.DailyDataResponse;
import com.barran.gank.service.beans.DatasResponse;
import com.barran.gank.service.beans.HistoryDates;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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
    
    @GET("{url}")
    Observable<DailyDataResponse> getDailyData(@Path("url") String url);
    
    @GET("{url}")
    Observable<DatasResponse> getDataByType(@Path("url") String url);
}
