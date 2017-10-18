package com.barran.gank.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.barran.gank.R
import com.barran.gank.app.App
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.bitmap
import com.barran.gank.app.IMAGE_PATH
import java.io.FileOutputStream


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
    Picasso.with(this.context).load(url).placeholder(R.mipmap.empty).error(R.mipmap.failpicture).into(this)
}

fun String.download() {
    if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
        "sdcard not exists!".toast()
        return
    }

    val lastIndex = this.lastIndexOf("/")
    var file: File? = null
    if (lastIndex > 0) {
        val fileName = this.subSequence(lastIndex + 1, this.length)
        val sdDir = Environment.getExternalStorageDirectory()
        val parent = File("$sdDir${File.separator}$IMAGE_PATH")
        if (!parent.exists()) {
            parent.mkdirs()
        }
        file = File("$sdDir${File.separator}$IMAGE_PATH${File.separator}$fileName")
        if (file.exists()) {
            "该图片已经下载过，请到sd卡/$IMAGE_PATH 查看".toast()
            return
        }
        val create = file.createNewFile()
        Log.v("download", "create file: ${file.absolutePath}  result: $create")
        if (!create) {
            "创建文件失败".toast()
            return
        }
    }

    Picasso.with(App.appContext).load(this).into(object : Target {
        override fun onBitmapFailed(errorDrawable: Drawable?) {
            Log.v("download", "onBitmapFailed $this")
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            Log.v("download", "onBitmapFailed $this")
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            Log.v("download", "onBitmapLoaded $bitmap")
            if (bitmap != null) {

                val outStream: FileOutputStream?
                try {
                    outStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                    outStream!!.close()

                    Log.v("download", "save bitmap suc")
                    "图片保存成功，路径为sd卡/$IMAGE_PATH".toast()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    })
}

class SpannableData(val spannable: Any, val start: Int, val end: Int) {
    var flags = SpannableString.SPAN_INCLUSIVE_INCLUSIVE
}

fun String.spannable(vararg spannable: SpannableData): SpannableString {
    val result = SpannableString(this)

    for (it in spannable) {
        result.setSpan(it.spannable, it.start, it.end, it.flags)
    }

    return result

}

const val FORMAT_DEFAULT = "yyyy/MM/dd"
const val FORMAT_YMD = "yyyy-MM-dd"
const val FORMAT_YMD_HMS = "yyyy/MM/dd HH:mm:ss"

fun Long.dateFormat(format: String = FORMAT_DEFAULT): String =
        SimpleDateFormat(format, Locale.CHINA).format(Date(this))

fun Date.dateFormat(format: String = FORMAT_DEFAULT): String =
        SimpleDateFormat(format, Locale.CHINA).format(this)

fun String.toTimeMillis(format: String = FORMAT_YMD): Long = SimpleDateFormat(format, Locale.CHINA).parse(this).time

fun String.toast(length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.appContext, this, length).show()
}
