package com.barran.gank.app

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.barran.gank.R
import com.barran.gank.libs.recycler.BaseRecyclerAdapter
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener
import com.barran.gank.api.ApiServiceImpl
import com.barran.gank.api.beans.DailyDataResponse
import com.barran.gank.api.beans.DataInfo
import com.barran.gank.api.beans.GankDataType
import com.barran.gank.libs.greendao.DataCache
import com.barran.gank.utils.dateFormat
import com.barran.gank.utils.load
import com.barran.gank.utils.toTimeMillis
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * 展示每日资讯
 *
 * Created by tanwei on 2017/10/1.
 */
class DailyContentFragment : Fragment() {

    companion object {
        const val EXTRA_DATE = "date"

        const val TYPE_CONTENT = 1
        const val TYPE_GROUP = 2
    }

    private val date = Calendar.getInstance(Locale.CHINA)

    private lateinit var image: ImageView

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: Adapter

    private val contentList = ArrayList<DataInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var time: String? = null
        if (arguments != null) {
            time = arguments.getString(EXTRA_DATE)
        }
        if (time != null) {
            date.timeInMillis = time.toTimeMillis()
        } else {
            date.timeInMillis = System.currentTimeMillis()
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_daily_info, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        loadDailyData()
    }

    private fun initView(view: View?) {
        image = view?.findViewById(R.id.fragment_daily_info_image) as ImageView
        recyclerView = view.findViewById(R.id.fragment_daily_info_list) as RecyclerView
        adapter = Adapter(object : RecyclerViewItemClickListener {
            override fun onItemClick(holder: BaseRecyclerHolder, position: Int) {
                val data = contentList[position]

                // add history
                DataCache.cache.insertHistoryData(data)

                if (data.url != null) {
                    viewInfo(activity, data)
                }
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {

            val margin = activity.resources.getDimensionPixelOffset(R.dimen.default_dimen_margin)

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                        state: RecyclerView.State?) {

                if (adapter.getItemViewType(
                        (view.layoutParams as RecyclerView.LayoutParams)
                                .viewLayoutPosition) == TYPE_GROUP) {

                    outRect.set(0, margin, 0, 0)
                }
            }
        })
    }

    private fun loadDailyData() {
        ApiServiceImpl.getDailyData(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH), object : Observer<DailyDataResponse> {
            override fun onSubscribe(d: Disposable) {
                Log.v("loadDailyData", "onSubscribe")
            }

            override fun onNext(t: DailyDataResponse) {
                Log.v("loadDailyData", "onNext size:${t.results.size}")
                t.results.iterator().forEach {
                    if (GankDataType.WELFARE.typeName == it.key) {
                        image.load(it.value[0].url)
                    } else {
                        val group = DataInfo()
                        group.type = it.key
                        contentList.add(group)
                        contentList.addAll(it.value)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onComplete() {
                Log.v("loadDailyData", "onComplete")
            }

            override fun onError(e: Throwable) {
                Log.v("loadDailyData", "onError")
            }

        })
    }

    private inner class Adapter(clickListener: RecyclerViewItemClickListener?) : BaseRecyclerAdapter(clickListener) {
        override fun createHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerHolder =
                when (viewType) {
                    TYPE_GROUP -> GroupHolder(activity.layoutInflater.inflate(R.layout.item_daily_info_group, parent, false))
                    TYPE_CONTENT -> ItemHolder(activity.layoutInflater.inflate(R.layout.item_daily_info_content, parent, false), itemClickListener)
                    else
                    -> throw IllegalArgumentException()
                }

        override fun onBindViewHolder(holder: BaseRecyclerHolder?, position: Int) {
            if (holder is ItemHolder) {
                holder.update(contentList[position])
            } else if (holder is GroupHolder) {
                holder.update(contentList[position])
            }
        }

        override fun getItemCount(): Int = contentList.size

        override fun getItemViewType(position: Int): Int =
                if (contentList[position].url == null) TYPE_GROUP else TYPE_CONTENT
    }

    private inner class GroupHolder constructor(itemView: View) : BaseRecyclerHolder(itemView, null) {

        private val tvTitle = itemView.findViewById(R.id.item_daily_info_group_type) as TextView

        private val divider = itemView.findViewById(R.id.item_daily_info_group_divider)

        fun update(data: DataInfo) {

            tvTitle.text = data.type
        }
    }
}

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
        image.load(data.images?.get(0))
        title.text = data.desc
        author.text = data.who
        time.text = data.publishedAt?.dateFormat()

        if (data.read)
            itemView.setBackgroundResource(R.drawable.bg_read)
        else {
            itemView.setBackgroundResource(R.drawable.bg_unread)
        }

        if (hideDivider) {
            divider.visibility = View.GONE
        } else {
            divider.visibility = View.VISIBLE
        }
    }
}