package com.barran.gank.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.api.beans.GankDataType

/**
 * config keys and values
 * Created by tanwei on 2017/10/1.
 */

const val CONFIG_KEY_LAST_REQUEST_HISTORY_TIME = "last_request_history_time"
// 重新请求历史间隔：3天
const val CONFIG_VALUE_REQUEST_HISTORY_INTERVAL = 3 * 24 * 60 * 60 * 1000L

const val EXTRA_URL = "url"

fun viewInfo(activity: Activity, data: DataInfo) {
    if (GankDataType.PASTTIME.typeName == data.type) {
        viewInWeb(activity, data.url)
    } else {
        val intent = Intent(activity, HtmlActivity::class.java)
        intent.putExtra(EXTRA_URL, data.url)
        activity.startActivity(intent)
    }
}

fun viewInWeb(activity: Activity, url: String?) {
    if(url.isNullOrBlank()){
        return
    }
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    activity.startActivity(intent)
}
