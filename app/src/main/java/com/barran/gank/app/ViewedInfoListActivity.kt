package com.barran.gank.app

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.ViewGroup

import com.barran.gank.R
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.libs.greendao.DataCache
import com.barran.gank.libs.recycler.*

class ViewedInfoListActivity : AppCompatActivity() {

    private lateinit var dataList: List<DataInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewed_info_list)

        setSupportActionBar(findViewById(R.id.activity_viewed_info_list_toolbar) as Toolbar)

        dataList = DataCache.cache.getHistoryDataList()

        val recyclerView = findViewById(R.id.fragment_viewed_info_list_recycler_view) as RecyclerView

        val adapter = DataAdapter(object : RecyclerViewItemClickListener {
            override fun onItemClick(holder: BaseRecyclerHolder, position: Int) {
                val data = dataList[position]
                // add history
                DataCache.cache.insertHistoryData(data)

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(data.url)
                startActivity(intent)
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemDecoration = VerticalItemDecoration()
        itemDecoration.setDividerHeight(resources.getDimensionPixelOffset(R.dimen.default_dimen_text))
        recyclerView.addItemDecoration(itemDecoration)
    }

    private inner class DataAdapter(clickListener: RecyclerViewItemClickListener?)
        : BaseRecyclerAdapter(clickListener) {
        override fun getItemCount(): Int = dataList.size

        override fun createHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerHolder =
                ItemHolder(layoutInflater.inflate(R.layout.item_daily_info_content, parent, false), itemClickListener)

        override fun onBindViewHolder(holder: BaseRecyclerHolder?, position: Int) {
            if (holder is ItemHolder) {
                holder.hideDivider = true
                holder.update(dataList[position])
            }
        }
    }
}
