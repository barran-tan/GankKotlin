package com.barran.gank.libs.recycler

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * 监听RecyclerView的滑动情况，判断是否上拉加载更多
 *
 * Created by tanwei on 2017/10/12.
 */
class RefreshScrollListener(private val listener: RefreshLoadMoreListener?) : RecyclerView.OnScrollListener() {

    private var lastVisibleItem = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val adapter = recyclerView.adapter
            if (adapter != null && lastVisibleItem + 1 == adapter.itemCount) {
                //滚动到底部了，可以进行数据加载等操作
                listener?.loadMore()
            }
        }
    }
}

interface RefreshLoadMoreListener {
    fun loadMore()
}