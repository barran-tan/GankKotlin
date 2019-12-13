package com.barran.gank.libs.recycler

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * 封装RecyclerAdapter支持item点击
 *
 * Created by tanwei on 2017/10/1.
 */

abstract class BaseRecyclerAdapter(var itemClickListener: RecyclerViewItemClickListener?) : RecyclerView.Adapter<BaseRecyclerHolder>(){

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder {

        return createHolder(parent, viewType)
    }

    abstract fun createHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder

}

open class BaseRecyclerHolder(view: View, itemClickListener: RecyclerViewItemClickListener?) : RecyclerView.ViewHolder(view) {
    init {
        if (itemClickListener != null) {
            view.setOnClickListener { itemClickListener.onItemClick(this, adapterPosition) }
        }
    }
}

interface RecyclerViewItemClickListener {
    fun onItemClick(holder: BaseRecyclerHolder, position: Int)
}