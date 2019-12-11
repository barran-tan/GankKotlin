package com.barran.gank.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.ViewGroup
import com.barran.gank.R
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.libs.greendao.DataCache
import com.barran.gank.libs.recycler.BaseRecyclerAdapter
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener
import com.barran.gank.libs.recycler.VerticalItemDecoration

class FavoriteListActivity : AppCompatActivity() {

    private lateinit var dataList: List<DataInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewed_info_list)

        setSupportActionBar(findViewById(R.id.activity_viewed_info_list_toolbar))

        dataList = DataCache.cache.getFavoriteDataList()

        val recyclerView = findViewById<RecyclerView>(R.id.fragment_viewed_info_list_recycler_view)

        val adapter = DataAdapter(object : RecyclerViewItemClickListener {
            override fun onItemClick(holder: BaseRecyclerHolder, position: Int) {
                val data = dataList[position]
                viewInfo(this@FavoriteListActivity, data)
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemDecoration = VerticalItemDecoration()
        itemDecoration.setDividerHeight(resources.getDimensionPixelOffset(R.dimen.default_dimen_text))
        recyclerView.addItemDecoration(itemDecoration)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    private inner class DataAdapter(clickListener: RecyclerViewItemClickListener?)
        : BaseRecyclerAdapter(clickListener) {
        override fun getItemCount(): Int = dataList.size

        override fun createHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder =
                ItemHolder(layoutInflater.inflate(R.layout.item_daily_info_content, parent, false), itemClickListener)

        override fun onBindViewHolder(holder: BaseRecyclerHolder, position: Int) {
            if (holder is ItemHolder) {
                holder.hideDivider = true
                holder.update(dataList[position], false)
            }
        }
    }
}