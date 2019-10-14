package com.barran.gank.app

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.barran.gank.R
import com.barran.gank.utils.download
import com.github.chrisbanes.photoview.PhotoView
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class BigImageActivity : AppCompatActivity() {

    private lateinit var imageList: List<String>
    private var index: Int = 0

    private lateinit var indexText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_image)

        if (intent.hasExtra(EXTRA_IMAGE_LIST)) {
            imageList = intent.getStringArrayListExtra(EXTRA_IMAGE_LIST)
            index = intent.getIntExtra(EXTRA_INDEX, 0)
        } else {
            finish()
            return
        }

        val viewPager = findViewById<ViewPager>(R.id.activity_big_image_view_pager)
        viewPager.adapter = ViewPageAdapter()
        viewPager.currentItem = index
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                index = position
                updateIndex()
            }

        })

        indexText = findViewById(R.id.activity_big_image_index)
        updateIndex()

        val download = findViewById<View>(R.id.activity_big_image_download)
        download.setOnClickListener {
            imageList[index].download()
        }
    }

    private fun updateIndex() {
        indexText.text = "${index + 1}/${imageList.size}"
    }

    inner class ViewPageAdapter : androidx.viewpager.widget.PagerAdapter() {
        private val cacheView = ArrayList<PhotoView>()

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            var view: View? = if (cacheView.size > 0) cacheView.removeAt(0) else null

            if (view == null) {
                view = PhotoView(this@BigImageActivity)
            }

            val photoViewAttacher = PhotoViewAttacher(view as PhotoView)

            Picasso.with(this@BigImageActivity).load(imageList[position]).config(Bitmap.Config.RGB_565).into(view, object : Callback {
                override fun onSuccess() {
                    photoViewAttacher.update()
                }

                override fun onError() {

                }
            })

            photoViewAttacher.setOnPhotoTapListener { _, _, _ -> this@BigImageActivity.finish() }

            view.setTag(position)

            container.addView(view)
            return view
        }

        override fun getCount(): Int = imageList.size

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as PhotoView
            container.removeView(view)
            cacheView.add(view)
        }

    }

}
