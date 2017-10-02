package com.barran.gank.app

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.barran.gank.R
import com.barran.gank.libs.recycler.BaseRecyclerAdapter
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener
import com.barran.gank.service.ApiServiceImpl
import com.barran.gank.service.beans.DataInfo
import com.barran.gank.service.beans.DatasResponse
import com.barran.gank.service.beans.GankDataType
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.ArrayList


/**
 * 分类数据列表页
 *
 * Created by tanwei on 2017/9/29.
 */
class DataListFragment(infoType: Int = GankDataType.ANDROID.ordinal) : Fragment() {

    private val pageCount = 10
    private var page = 0
    private var type: String = GankDataType.getName(infoType)

    private val dataList = ArrayList<DataInfo>()

    lateinit private var refreshLayout: SwipeRefreshLayout
    lateinit private var recyclerView: RecyclerView
    lateinit private var adapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_info_list, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout = view?.findViewById(R.id.fragment_info_list_refresh_layout) as SwipeRefreshLayout
        recyclerView = view.findViewById(R.id.fragment_info_list_recycler_view) as RecyclerView

        adapter = DataAdapter(object : RecyclerViewItemClickListener {
            override fun onItemClick(holder: BaseRecyclerHolder, position: Int) {
                val data = dataList[position]
                val intent = Intent(activity, HtmlActivity::class.java)
                intent.putExtra(HtmlActivity.EXTRA_URL, data.url)
                startActivity(intent)
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        refreshLayout.setOnRefreshListener {
            page = 0
            getData()
        }

        getData(page++)
        refreshLayout.isRefreshing = true
    }

    private fun getData(page: Int = 0) {
        ApiServiceImpl.getDataByType(type, pageCount, page, object : Observer<DatasResponse> {
            override fun onComplete() {
                refreshLayout.isRefreshing = false
            }

            override fun onError(e: Throwable) {
                refreshLayout.isRefreshing = false
            }

            override fun onNext(t: DatasResponse) {
                refreshLayout.isRefreshing = false

                dataList.addAll(t.results)
                adapter.notifyDataSetChanged()
            }

            override fun onSubscribe(d: Disposable) {
            }
        })
    }

    private inner class DataAdapter(clickListener: RecyclerViewItemClickListener?)
        : BaseRecyclerAdapter(clickListener) {
        override fun onBindViewHolder(holder: BaseRecyclerHolder?, position: Int) {
            if (holder is ItemHolder) {
                holder.update(dataList[position])
            }
        }

        override fun createHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerHolder =
                ItemHolder(activity.layoutInflater.inflate(R.layout.item_daily_info_content, parent, false), itemClickListener)

        override fun getItemCount(): Int = dataList.size

    }
}
