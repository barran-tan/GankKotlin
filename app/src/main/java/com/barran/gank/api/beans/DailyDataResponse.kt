package com.barran.gank.api.beans


/**
 * 对应每日数据的解析后的bean
 *
 * Created by tanwei on 2017/10/2.
 */
class DailyDataResponse {

    var category: Array<String>? = null
    var error = false
    var results = HashMap<String, List<DataInfo>>()
}