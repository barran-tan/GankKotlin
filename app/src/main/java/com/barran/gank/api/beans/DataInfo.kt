package com.barran.gank.api.beans

import com.barran.gank.libs.greendao.DataInfoEntity
import java.util.*
import kotlin.collections.ArrayList

/**
 * data from gank.io
 *
 * Created by tanwei on 2017/9/27.
 */

// "_id": "59b9da64421aa911847a039a",
// "createdAt": "2017-09-14T09:24:52.83Z",
// "desc":
// "\u8be6\u7ec6\u8bb2\u89e3Launcher\u6574\u4e2a\u5f00\u53d1\u6d41\u7a0b\u7684\u7cfb\u5217\u6559\u7a0b\u3002",
// "publishedAt": "2017-09-26T12:12:07.813Z",
// "source": "web",
// "type": "Android",
// "url": "http://www.codemx.cn/tags/Launcher/",
// "used": true,
// "who": "YUCHUAN"
class DataInfo {

    constructor()

    constructor(data: DataInfoEntity) {
        _id = data.infoId
        type = data.type
        createAt = Date(data.createTime)
        publishedAt = Date(data.publishTime)
        desc = data.desc
        url = data.linkUrl
        who = data.author
        images = if(data.images != null) arrayOf(data.images) else null

        read = data.isRead
    }

    // server field
    var _id: String? = null

    var type: String = "Android"

    var createAt: Date? = null
    var publishedAt: Date? = null

    var desc: String? = null

    var url: String? = null

    var who: String? = null

    var used = false

    var sourse: String? = null
    var images: Array<String>? = null

    // custom field
    var read = false
}

enum class GankDataType(val typeName: String) {
    ALL("全部"), ANDROID("Android"), IOS("IOS"), PASTTIME("休息视频"), WELFARE("福利"),
    EXPANDINFOMATION("拓展资源"),
    FRONTEND("前端"), RECOMMEND("瞎推荐"), APP("APP");

    companion object {
        fun getName(type: Int): String {
            return when (type) {
                ALL.ordinal -> "全部"
                ANDROID.ordinal -> "Android"
                IOS.ordinal -> "IOS"
                PASTTIME.ordinal -> "休息视频"
                WELFARE.ordinal -> "福利"
                EXPANDINFOMATION.ordinal -> "拓展资源"
                FRONTEND.ordinal -> "前端"
                RECOMMEND.ordinal -> "瞎推荐"
                APP.ordinal -> "APP"
                else -> "未分类"
            }
        }
    }
}

class DatasResponse {
    var error = false
    var results = ArrayList<DataInfo>()
}