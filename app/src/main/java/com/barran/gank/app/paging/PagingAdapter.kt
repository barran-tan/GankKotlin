package com.barran.gank.app.paging

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.barran.gank.R
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.app.ItemHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener

/**
 * 分页加载adapter
 */
class PagingAdapter(private val itemClickListener: RecyclerViewItemClickListener?) : PagedListAdapter<DataInfo, ItemHolder>(diffCallback) {

    companion object {

        const val TAG = "paging"

        private val diffCallback = object : DiffUtil.ItemCallback<DataInfo>() {
            override fun areItemsTheSame(oldItem: DataInfo, newItem: DataInfo): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: DataInfo, newItem: DataInfo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder = ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_daily_info_content, parent, false), itemClickListener)

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.hideDivider = true
        val data = super.getItem(position)
        data?.let {
            holder.update(data)
        } ?: Log.v("PagingAdapter", "null data at $position")

        Log.v(TAG, "onBind $position")
    }

    public override fun getItem(position: Int): DataInfo? {
        return super.getItem(position)
    }
}