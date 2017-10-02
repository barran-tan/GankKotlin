package com.barran.gank.app

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.barran.gank.R
import com.barran.gank.app.ImageListFragment.OnImageClick
import com.barran.gank.utils.push

/**
 * 展示图片的界面（图片列表和浏览大图）
 *
 * Created by tanwei on 2017/10/1.
 */
class ImagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_empty)

        val fragment = ImageListFragment()
        fragment.imageClickListener = object:OnImageClick{
            override fun onImageClick(url: String) {

            }

        }
        push(R.id.activity_empty_container, fragment)
    }
}