package com.barran.gank.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.barran.gank.R
import com.barran.gank.libs.recycler.BaseRecyclerAdapter
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener
import com.barran.gank.api.ApiServiceImpl
import com.barran.gank.api.beans.HistoryDates
import com.vivian.timelineitemdecoration.itemdecoration.DotItemDecoration
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


/**
 * 展示有历史数据的日期列表
 *
 * Created by tanwei on 2017/10/1.
 */
class HistoryDatesFragment : Fragment() {

    var datesList = ArrayList<String>()

    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val lastTime = SPUtils.getLong(CONFIG_KEY_LAST_REQUEST_HISTORY_TIME, 0L)
//        if (lastTime > 0 && System.currentTimeMillis() - lastTime > CONFIG_VALUE_REQUEST_HISTORY_INTERVAL
//                || !loadLocalHistoryDates()) {
//
//        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_history_dates, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view?.findViewById(R.id.fragment_history_dates_recycler_view) as RecyclerView
        adapter = Adapter(object : RecyclerViewItemClickListener {
            override fun onItemClick(holder: BaseRecyclerHolder, position: Int) {
                val intent = Intent(activity, DailyContentActivity::class.java)
                intent.putExtra(DailyContentFragment.EXTRA_DATE, datesList[position])
                startActivity(intent)
            }

        })
        recycler.adapter = adapter

        recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = getDecoration()
        decoration.setSpanIndexListener { view, spanIndex -> view.setBackgroundResource(if (spanIndex == 0) R.drawable.pop_left else R.drawable.pop_right) }
        recycler.addItemDecoration(decoration)

        getHistoryDates()
    }

    private fun getDecoration(): DotItemDecoration = DotItemDecoration.Builder(activity)
            .setOrientation(DotItemDecoration.VERTICAL)//if you want a horizontal item decoration,remember to set horizontal orientation to your LayoutManager
            .setItemStyle(DotItemDecoration.STYLE_DRAW)//choose to draw or use resource
            .setTopDistance(20f)//dp
            .setItemInterVal(10f)//dp
            .setItemPaddingLeft(10f)//default value equals to item interval value
            .setItemPaddingRight(10f)//default value equals to item interval value
            .setDotColor(Color.WHITE)
            .setDotRadius(2)//dp
            .setDotPaddingTop(0)
            .setDotInItemOrientationCenter(false)//set true if you want the dot align center
            .setLineColor(Color.RED)//the color of vertical line
            .setLineWidth(1f)//dp
            .setEndText("END")//set end text
            .setTextColor(Color.WHITE)
            .setTextSize(10f)//sp
            .setDotPaddingText(2f)//dp.The distance between the last dot and the end text
            .setBottomDistance(40f)//you can add a distance to make bottom line longer
            .create()

    // 从数据库加载有历史数据的日期列表
    private fun loadLocalHistoryDates(): Boolean {
//        val results = RealmHelper.get().where(DateInfo::class.java).findAllAsync()
//        return if (results.count() > 0) {
//            datesList.addAll(results)
//            adapter.notifyDataSetChanged()
//            true
//        } else {
//            false
//        }
        return false
    }

    private fun getHistoryDates() {
        ApiServiceImpl.getHistoryDates(object : Observer<HistoryDates> {
            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }

            override fun onNext(t: HistoryDates) {
                datesList.addAll(t.results!!)
                adapter.notifyDataSetChanged()
            }

            override fun onSubscribe(d: Disposable) {
            }

        })
    }

    inner class Adapter(clickListener: RecyclerViewItemClickListener?) : BaseRecyclerAdapter(clickListener) {
        override fun getItemCount(): Int = datesList.size

        override fun createHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerHolder =
                DateHolder(activity.layoutInflater.inflate(R.layout.item_date, parent, false), itemClickListener)

        override fun onBindViewHolder(holder: BaseRecyclerHolder?, position: Int) {
            if (holder is DateHolder) {
                holder.date.text = datesList[position]
            }
        }

    }

    inner class DateHolder(view: View, clickListener: RecyclerViewItemClickListener?) : BaseRecyclerHolder(view, clickListener) {
        var date = view.findViewById(R.id.item_date_tv) as TextView
    }
}
