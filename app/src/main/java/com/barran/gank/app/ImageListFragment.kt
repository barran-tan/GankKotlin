package com.barran.gank.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.barran.gank.R
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener
import shivam.developer.featuredrecyclerview.FeatureLinearLayoutManager
import shivam.developer.featuredrecyclerview.FeaturedRecyclerView

/**
 * 图片列表界面
 *
 * Created by tanwei on 2017/10/1.
 */
class ImageListFragment : Fragment() {

    var imageClickListener: OnImageClick? = null

    private lateinit var adapter: ImageAdapter

    private val images = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_image_list, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var recyclerView = view?.findViewById(R.id.fragment_image_list_recycler_view) as FeaturedRecyclerView
        recyclerView.layoutManager = FeatureLinearLayoutManager(activity)
        adapter = ImageAdapter(activity, images, object : RecyclerViewItemClickListener {
            override fun onItemClick(holder: BaseRecyclerHolder, position: Int) {
                if (imageClickListener != null) {
                    imageClickListener!!.onImageClick(images[position])
                }
            }
        })
        recyclerView.adapter = adapter
    }

    private fun loadImagesFromLocal(){

    }

    private fun getImages(){

    }

    interface OnImageClick {
        fun onImageClick(url: String)
    }
}