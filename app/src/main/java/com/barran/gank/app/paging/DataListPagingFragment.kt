package com.barran.gank.app.paging

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barran.gank.R
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.app.viewInfo
import com.barran.gank.libs.greendao.DataCache
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener
import com.barran.gank.libs.recycler.VerticalItemDecoration

/**
 * 使用paging实现分页加载
 *
 * create by tanwei@bigo.sg
 * on 2019/12/12
 */
class DataListPagingFragment(private val type: String) : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: PagingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_info_list_paging, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.fragment_info_list_recycler_view)

        adapter = PagingAdapter(object : RecyclerViewItemClickListener {
            override fun onItemClick(holder: BaseRecyclerHolder, position: Int) {
                val data = adapter.getItem(position)
                data?.let {
                    // add history
                    DataCache.cache.insertHistoryData(data)
                    // set read
                    data.url?.let { DataCache.cache.setRead(data.url!!, true) }

                    adapter.notifyItemChanged(position)

                    viewInfo(activity!!, data)
                }
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val itemDecoration = VerticalItemDecoration()
        itemDecoration.setDividerHeight(resources.getDimensionPixelOffset(R.dimen.default_dimen_text))
        recyclerView.addItemDecoration(itemDecoration)

        Log.i(PagingAdapter.TAG, "onViewCreated : $type")

        val builder = PagedList.Config.Builder()
        builder.setPageSize(10).setPrefetchDistance(5).setInitialLoadSizeHint(10).setEnablePlaceholders(true)
        val dataList = LivePagedListBuilder<Int, DataInfo>(InfoDataFactory(type), builder.build()).build()

        dataList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

}