package com.barran.gank.app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.barran.gank.R
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener
import com.barran.gank.libs.recycler.RefreshLoadMoreListener
import com.barran.gank.libs.recycler.RefreshScrollListener
import com.barran.gank.api.ApiServiceImpl
import com.barran.gank.api.beans.DataResponse
import com.barran.gank.api.beans.GankDataType
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shivam.developer.featuredrecyclerview.FeatureLinearLayoutManager
import shivam.developer.featuredrecyclerview.FeaturedRecyclerView

/**
 * 图片列表界面
 *
 * Created by tanwei on 2017/10/1.
 */
class ImageListFragment : Fragment() {

    companion object {
        const val pageCount = 10
    }

    private lateinit var adapter: ImageAdapter

    private val images = ArrayList<String>()

    private var mPage = 1

    private var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_image_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById(R.id.fragment_image_list_recycler_view) as FeaturedRecyclerView
        recyclerView.layoutManager = FeatureLinearLayoutManager(activity)
        adapter = ImageAdapter(activity!!, images, object : RecyclerViewItemClickListener {
            override fun onItemClick(holder: BaseRecyclerHolder, position: Int) {

                val intent = Intent(activity, BigImageActivity::class.java)
                intent.putStringArrayListExtra(EXTRA_IMAGE_LIST, images)
                intent.putExtra(EXTRA_INDEX, position)
                startActivity(intent)
            }
        })
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(RefreshScrollListener(object : RefreshLoadMoreListener {
            override fun loadMore() {
                if (isLoading) {
                    return
                }
                isLoading = true
                getImages(mPage++)
                Log.i("ImageList", "loadMore nextPage $mPage")
            }

        }))

        getImages(mPage++)
    }

    private fun getImages(page: Int = 0) {
        Log.i("ImageList", "getImages page : $page")
        ApiServiceImpl.getDataByType(GankDataType.WELFARE.typeName, pageCount, page, object : Observer<DataResponse> {
            override fun onComplete() {
                Log.v("getImages", "onComplete")
                isLoading = false
            }

            override fun onError(e: Throwable) {
                Log.v("getImages", "onError")
                isLoading = false
            }

            override fun onNext(t: DataResponse) {
                isLoading = false
                Log.v("getImages", "results size:${t.results.size}")

                t.results.filter { !it.url.isNullOrEmpty() }.forEach { images.add(it.url!!) }

                Log.v("getImages", "image size:${images.size}")

                adapter.notifyDataSetChanged()
            }

            override fun onSubscribe(d: Disposable) {
                Log.v("getImages", "onSubscribe")
            }
        })
    }
}