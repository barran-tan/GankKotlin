//package com.barran.gank.libs.recycler
//
//import android.content.Context
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ProgressBar
//import com.barran.gank.R
//
///**
// * 封装recycler adapter，实现上拉加载更多功能
// *
// * Created by tanwei on 2017/9/29.
// */
//abstract class RefreshRecyclerAdapter<T>(private var dataList: List<T>?, private var recyclerView: RecyclerView, clickListener: RecyclerViewItemClickListener?) : BaseRecyclerAdapter(clickListener) {
//
//    companion object {
//        private val VIEW_TYPE_DEFAULT = 0
//        private val VIEW_TYPE_LOADING = 1
//    }
//
//    var loadMoreListener: LoadMoreListener? = null
//
//    var isLoading = false
//    private var lastVisibleItemPosition = -1
//    private var totalItemCount: Int = 0
//
//    //当前滚动的position下面最小的items的临界值
//    private val visibleThreshold = 5
//
//    init {
//        if (recyclerView.layoutManager is LinearLayoutManager) {
//            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//
//                    val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
//                    totalItemCount = layoutManager.itemCount
//                    lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
//
//                    Log.v("test", "total = $totalItemCount , lastVisible = $lastVisibleItemPosition")
//                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
//                        //此时是刷新状态
//                        loadMoreListener?.loadMore()
//                        isLoading = true
//                    }
//                }
//            })
//        }
//    }
//
//    override fun createHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerHolder {
//        var holder = when (viewType) {
//            VIEW_TYPE_LOADING -> MyProgressViewHolder(LayoutInflater.from(recyclerView.context).inflate(R.layout.item_loading_footer, parent, false), itemClickListener)
//            else -> getViewHolder(recyclerView.context, parent, viewType)
//        }
//
//        return holder
//    }
//
//    abstract fun getViewHolder(context: Context, parent: ViewGroup?, viewType: Int): BaseRecyclerHolder
//
//    override fun onBindViewHolder(holder: BaseRecyclerHolder?, position: Int) {
//        if (holder != null && holder is MyProgressViewHolder) {
//            holder.pb.isIndeterminate = true
//        }
//
//        holder?.itemView?.tag = position
//    }
//
//    override fun getItemCount(): Int = if (dataList != null) {
//        dataList!!.size
//    } else 0
//
//    //根据不同的数据返回不同的viewType
//    override fun getItemViewType(position: Int): Int {
//
//        return if (dataList?.get(position) != null)
//            VIEW_TYPE_LOADING
//        else VIEW_TYPE_DEFAULT
//    }
//
//    class MyProgressViewHolder(itemView: View, clickListener: RecyclerViewItemClickListener?) : BaseRecyclerHolder(itemView, clickListener) {
//
//        var pb: ProgressBar = itemView.findViewById(R.id.pb) as ProgressBar
//
//    }
//
//}
//
//interface LoadMoreListener {
//    fun loadMore()
//}
//
//interface OnItemClickListener {
//    fun onItemClick(view: View, position: Int)
//}