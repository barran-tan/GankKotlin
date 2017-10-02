package com.barran.gank.utils

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.widget.ImageView
import com.barran.gank.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

/**
 * 扩展函数
 *
 * Created by tanwei on 2017/9/29.
 */

fun AppCompatActivity.push(containerId: Int, fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(containerId, fragment)
    transaction.commit()
}

fun ImageView.load(url: String?) {
    Picasso.with(this.context).load(url).placeholder(R.mipmap.load).error(R.mipmap.failpicture).into(this)
}

class SpannableData(val spannable: Any, val start: Int, val end: Int) {
    var flags = SpannableString.SPAN_INCLUSIVE_INCLUSIVE
}

fun String.spanable(vararg spannable: SpannableData): SpannableString {
    val result = SpannableString(this)

    for (it in spannable) {
        result.setSpan(it.spannable, it.start, it.end, it.flags)
    }

    return result
}

const val FORMAT_DEFAULT = "yyyy/MM/dd"
const val FORMAT_YMD = "yyyy-MM-dd"
const val FORMAT_YMD_HM = "yyyy/MM/dd HH:mm"

fun Long.dateFormat(format: String = FORMAT_DEFAULT): String =
        SimpleDateFormat(format, Locale.CHINA).format(Date(this))

fun String.toTimemillis(format: String = FORMAT_YMD): Long = SimpleDateFormat(format, Locale.CHINA).parse(this).time
