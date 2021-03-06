package com.barran.gank.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.barran.gank.R
import com.barran.gank.api.ApiServiceImpl
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.api.beans.DataResponse
import com.barran.gank.api.beans.GankDataType
import com.barran.gank.libs.greendao.DataCache
import com.barran.gank.libs.recycler.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.*


/**
 * 分类数据列表页
 *
 * Created by tanwei on 2017/9/29.
 */
class DataListFragment : Fragment() {

    private val mPageCount = 10
    private var mPage = 1
    private lateinit var type: String

    private val dataList = ArrayList<DataInfo>()

    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        type = if (arguments != null) {
            val infoType = arguments!!.getInt(EXTRA_INFO_TYPE, GankDataType.ANDROID.ordinal)
            GankDataType.getName(infoType)
        } else {
            GankDataType.ANDROID.typeName
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_info_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout = view.findViewById(R.id.fragment_info_list_refresh_layout) as SwipeRefreshLayout
        recyclerView = view.findViewById(R.id.fragment_info_list_recycler_view) as RecyclerView

        recyclerView.addOnScrollListener(RefreshScrollListener(object : RefreshLoadMoreListener {
            override fun loadMore() {
                if (adapter.isLoading) {
                    return
                }
                adapter.showLoading()
                getData(mPage++)
                Log.i("DataList", "loadMore nextPage $mPage")
            }

        }))

        adapter = DataAdapter(dataList, object : RecyclerViewItemClickListener {
            override fun onItemClick(holder: BaseRecyclerHolder, position: Int) {
                val data = dataList[position]
                // add history
                DataCache.cache.insertHistoryData(data)
                // set read
                data.url?.let { DataCache.cache.setRead(data.url!!, true) }

                adapter.notifyItemChanged(position)

                viewInfo(activity!!, data)
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val itemDecoration = VerticalItemDecoration()
        itemDecoration.setDividerHeight(resources.getDimensionPixelOffset(R.dimen.default_dimen_text))
        recyclerView.addItemDecoration(itemDecoration)

        refreshLayout.setOnRefreshListener {
            mPage = 1
            getData(mPage++)
        }

        Log.i("DataList", "onViewCreated : $type")
        getData(mPage++)
        refreshLayout.isRefreshing = true
    }

    private fun getData(page: Int) {

        Log.i("DataList", "getData  $page")

        ApiServiceImpl.getDataByType(type, mPageCount, page, object : Observer<DataResponse> {
            override fun onComplete() {
                Log.v("getData", "onComplete")
                refreshLayout.isRefreshing = false
                adapter.showLoadMore()
            }

            override fun onError(e: Throwable) {
                Log.v("getData", "onError")
                refreshLayout.isRefreshing = false
                adapter.showLoadMore()
            }

            override fun onNext(t: DataResponse) {
                Log.v("getData", "onNext")
                refreshLayout.isRefreshing = false
                adapter.showLoadMore()

                if (page == 1 && dataList.isNotEmpty()) {
                    dataList.clear()
                }

                dataList.addAll(t.results)
                adapter.notifyDataSetChanged()
            }

            override fun onSubscribe(d: Disposable) {
            }
        })
    }

    private inner class DataAdapter(dataList: List<DataInfo>, clickListener: RecyclerViewItemClickListener?)
        : RefreshRecyclerAdapter<DataInfo>(dataList, clickListener) {
        override fun createContentHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerHolder =
                ItemHolder(activity!!.layoutInflater.inflate(R.layout.item_daily_info_content, parent, false), itemClickListener)

        override fun onBindViewHolder(holder: BaseRecyclerHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            if (holder is ItemHolder) {
                holder.hideDivider = true
                holder.update(dataList[position])
            }
        }
    }
}
