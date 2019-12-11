package com.barran.gank.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
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
        setSupportActionBar(findViewById(R.id.activity_empty_toolbar))

        push(R.id.activity_empty_container, HistoryDatesFragment())
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}