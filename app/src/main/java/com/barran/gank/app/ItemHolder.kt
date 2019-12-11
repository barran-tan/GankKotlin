package com.barran.gank.app

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.barran.gank.R
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.libs.greendao.DataCache
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener
import com.barran.gank.utils.dateFormat
import com.barran.gank.utils.load

class ItemHolder(itemView: View, clickListener: RecyclerViewItemClickListener?) : BaseRecyclerHolder(itemView, clickListener) {

    private val image = itemView
            .findViewById(R.id.item_daily_info_content_image) as ImageView

    private val title: TextView = itemView.findViewById(R.id.item_daily_info_content_title) as TextView
    private val author: TextView = itemView
            .findViewById(R.id.item_daily_info_content_author) as TextView
    private val time: TextView = itemView
            .findViewById(R.id.item_daily_info_content_time) as TextView

    private val divider: View = itemView.findViewById(R.id.item_daily_info_content_divider)

    var hideDivider = false

    fun update(data: DataInfo) {
        update(data, true)
    }

    fun update(data: DataInfo, showRead: Boolean) {
        if (data.images != null && data.images!!.isNotEmpty()) {
            image.load(data.images!![0])
        } else {
            image.setImageResource(R.mipmap.empty)
        }
        title.text = data.desc
        author.text = data.who ?: "unknown"
        time.text = data.publishedAt?.dateFormat() ?: "unknown"

        if (showRead) {
            val read = data.url != null && DataCache.cache.isRead(data.url!!)
            if (read)
                itemView.setBackgroundResource(R.drawable.bg_read)
            else {
                itemView.setBackgroundResource(R.drawable.bg_unread)
            }
        } else {
            itemView.setBackgroundResource(R.drawable.bg_unread)
        }

        if (hideDivider) {
            divider.visibility = View.GONE
        } else {
            divider.visibility = View.VISIBLE
        }
    }
}