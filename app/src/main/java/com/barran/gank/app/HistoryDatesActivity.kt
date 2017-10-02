package com.barran.gank.app

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.barran.gank.R
import com.barran.gank.utils.push

/**
 * 展示有历史数据的日期列表
 *
 * Created by tanwei on 2017/10/2.
 */
class HistoryDatesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_empty)

        push(R.id.activity_empty_container, HistoryDatesFragment())
    }
}