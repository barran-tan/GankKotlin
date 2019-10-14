package com.barran.gank.libs.recycler

import androidx.annotation.CallSuper
import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import com.barran.gank.R


/**
 * 封装显示上拉加载更多ui的adapter（:BaseRecyclerAdapter：RecyclerView.Adapter）
 *
 * Created by tanwei on 2017/10/12.
 */
abstract class RefreshRecyclerAdapter<T>(private val dataList: List<T>, itemClickListener: RecyclerViewItemClickListener?) : BaseRecyclerAdapter(itemClickListener) {

    companion object {
        const val TYPE_CONTENT = 0
        const val TYPE_FOOTER = 10000
    }

    private var pbLoading: ProgressBar? = null
    private var tvLoadMore: TextView? = null

    var isLoading = false

    /**
     * 显示正在加载的进度条，滑动到底部时，调用该方法，上拉就显示进度条，隐藏"上拉加载更多"
     */
    fun showLoading() {
        if (isLoading) {
            return
        } else {
            isLoading = true
        }
        if (pbLoading != null && tvLoadMore != null) {
            (pbLoading as ProgressBar).visibility = View.VISIBLE
            (tvLoadMore as TextView).visibility = View.GONE
        }
    }

    /**
     * 显示上拉加载的文字，当数据加载完毕，调用该方法，隐藏进度条，显示“上拉加载更多”
     */
    fun showLoadMore() {

        if (isLoading) {
            isLoading = false
        } else {
            return
        }

        if (pbLoading != null && tvLoadMore != null) {
            (pbLoading as ProgressBar).visibility = View.GONE
            (tvLoadMore as TextView).visibility = View.VISIBLE
        }
    }

    final override fun createHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder {
        return if (viewType == TYPE_FOOTER) {
            val footer = LayoutInflater.from(parent.context).inflate(R.layout.item_refresh_list_footer, parent, false)
            FooterViewHolder(footer, itemClickListener)
        } else {
            createContentHolder(parent, viewType)
        }
    }

    abstract fun createContentHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerHolder

    @CallSuper
    override fun onBindViewHolder(holder: BaseRecyclerHolder, position: Int) {
        if (position == dataList.size) {
            holder as FooterViewHolder
            pbLoading = holder.progressBarLoading
            tvLoadMore = holder.tvLoadMore
        }
    }

    override fun getItemCount(): Int = if (dataList.isEmpty()) 0 else (dataList.size + 1)

    override fun getItemViewType(position: Int): Int {
        return if (position == dataList.size) TYPE_FOOTER
        else TYPE_CONTENT
    }

    /**
     * footer的ViewHolder
     */
    class FooterViewHolder(itemView: View, itemClickListener: RecyclerViewItemClickListener?) : BaseRecyclerHolder(itemView, itemClickListener) {
        val tvLoadMore: TextView = itemView.findViewById(R.id.item_refresh_list_footer_tv) as TextView
        val progressBarLoading: ProgressBar = itemView.findViewById(R.id.item_refresh_list_footer_progressbar) as ProgressBar
    }
}