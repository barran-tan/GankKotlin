package com.barran.gank.app

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.api.beans.GankDataType

/**
 * config keys and values
 * Created by tanwei on 2017/10/1.
 */

const val CONFIG_KEY_LAST_REQUEST_HISTORY_TIME = "last_request_history_time"
// 重新请求历史间隔：3天
const val CONFIG_VALUE_REQUEST_HISTORY_INTERVAL = 3 * 24 * 60 * 60 * 1000L

const val IMAGE_PATH = "gank.images"

const val EXTRA_URL = "url"
const val EXTRA_IMAGE_LIST = "image_list"
const val EXTRA_INDEX = "index"

const val EXTRA_INFO_TYPE = "extra_info_type"

fun viewInfo(activity: FragmentActivity, data: DataInfo) {
    if (GankDataType.PAST_TIME.typeName == data.type) {
        viewInWeb(activity, data.url)
    } else {
        val intent = Intent(activity, HtmlActivity::class.java)
        intent.putExtra(EXTRA_URL, data.url)
        activity.startActivity(intent)
    }
}

fun viewInWeb(activity: FragmentActivity, url: String?) {
    if(url.isNullOrBlank()){
        return
    }
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    activity.startActivity(intent)
}
