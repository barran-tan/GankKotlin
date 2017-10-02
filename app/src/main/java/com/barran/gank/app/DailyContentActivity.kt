package com.barran.gank.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.barran.gank.R
import com.barran.gank.utils.push

/**
 * 展示每日内容
 *
 * Created by tanwei on 2017/10/1.
 */
class DailyContentActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_empty)
        val fragment = DailyContentFragment()
        fragment.arguments = intent.extras
        push(R.id.activity_empty_container, fragment)
    }
}