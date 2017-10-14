package com.barran.gank.api.beans

//import io.realm.RealmObject
//import io.realm.annotations.PrimaryKey

/**
 * 有数据的日期
 *
 * Created by tanwei on 2017/9/27.
 */
class DateInfo {

    var date: String? = null
    var error = false
}

class HistoryDates {
    var error = false
    var results: ArrayList<String>? = null
}