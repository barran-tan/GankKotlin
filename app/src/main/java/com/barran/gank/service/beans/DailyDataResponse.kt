package com.barran.gank.service.beans

import java.util.ArrayList

/**
 * 对应每日数据的解析后的bean
 *
 * Created by tanwei on 2017/10/2.
 */
class DailyDataResponse {

    var category: Array<String>? = null
    var error = false
    var results = ArrayList<Map<String, List<DataInfo>>>()
}