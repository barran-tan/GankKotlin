package com.barran.gank.app

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import com.barran.gank.R
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.libs.greendao.DataCache
import com.barran.gank.libs.recycler.BaseRecyclerAdapter
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener
import com.barran.gank.libs.recycler.VerticalItemDecoration
import com.barran.gank.utils.toast

const val SEARCH_TITLE = 0
const val SEARCH_AUTHOR = 1

class ViewedInfoListActivity : AppCompatActivity() {

    private lateinit var dataList: List<DataInfo>

    private lateinit var adapter: DataAdapter

    private var searchList: ArrayList<DataInfo> = ArrayList()

    private lateinit var searchMenu: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewed_info_list)

        setSupportActionBar(findViewById(R.id.activity_viewed_info_list_toolbar) as Toolbar)

        dataList = DataCache.cache.getHistoryDataList()

        val recyclerView = findViewById(R.id.fragment_viewed_info_list_recycler_view) as RecyclerView

        adapter = DataAdapter(object : RecyclerViewItemClickListener {
            override fun onItemClick(holder: BaseRecyclerHolder, position: Int) {
                val data = dataList[position]
                viewInfo(this@ViewedInfoListActivity, data)
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemDecoration = VerticalItemDecoration()
        itemDecoration.setDividerHeight(resources.getDimensionPixelOffset(R.dimen.default_dimen_text))
        recyclerView.addItemDecoration(itemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_viewed, menu)
        searchMenu = menu!!.findItem(R.id.menu_search)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_search -> {
                if (searchList.size > 0) {
                    searchList.clear()
                    adapter.notifyDataSetChanged()
                    searchMenu.setIcon(R.mipmap.search)
                } else {
                    inputSearch()
                }
            }
            R.id.menu_clear -> confirmClear()
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    private fun confirmClear() {
        AlertDialog.Builder(this).setMessage(R.string.dialog_message_clear_history)
                .setNegativeButton(R.string.dialog_cancel, null)
                .setPositiveButton(R.string.dialog_ok) { _, _ ->
                    DataCache.cache.clearHistoryDataList()

                    finish()
                }.show()
    }

    private fun inputSearch() {
        val editText = EditText(this)
        editText.setHint(R.string.dialog_message_input)
        editText.setPadding(60, 100, 60, 20)
        AlertDialog.Builder(this).setView(editText)
                .setPositiveButton(R.string.dialog_search_title) { _, _ ->
                    val input = editText.text.toString()
                    if (!TextUtils.isEmpty(input)) {
                        search(input, SEARCH_TITLE)
                    }
                }.setNeutralButton(R.string.dialog_search_author) { _, _ ->
            val input = editText.text.toString()
            if (!TextUtils.isEmpty(input)) {
                search(input, SEARCH_AUTHOR)
            }
        }
                .show()
    }

    private fun search(input: String, searchType: Int) {
        if (searchList.size > 0) searchList.clear()
        var resultCount = 0
        when (searchType) {
            SEARCH_TITLE -> {
                searchList.addAll(dataList.filter { !TextUtils.isEmpty(it.desc) && it.desc!!.contains(input, true) })

                resultCount = searchList.size

            }
            SEARCH_AUTHOR -> {
                searchList.addAll(dataList.filter { !TextUtils.isEmpty(it.who) && it.who!!.contains(input, true) })
                resultCount = searchList.size
            }
        }

        if (resultCount > 0) {
            adapter.notifyDataSetChanged()

            searchMenu.setIcon(R.mipmap.cancel)
        }

        "搜索到$resultCount 条记录".toast()
    }

    private inner class DataAdapter(clickListener: RecyclerViewItemClickListener?)
        : BaseRecyclerAdapter(clickListener) {
        override fun getItemCount(): Int = if (searchList.size > 0) searchList.size else dataList.size

        override fun createHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerHolder =
                ItemHolder(layoutInflater.inflate(R.layout.item_daily_info_content, parent, false), itemClickListener)

        override fun onBindViewHolder(holder: BaseRecyclerHolder?, position: Int) {
            if (holder is ItemHolder) {
                holder.hideDivider = true
                val data = if (searchList.size > 0) searchList[position] else dataList[position]
                holder.update(data, false)
            }
        }
    }
}
