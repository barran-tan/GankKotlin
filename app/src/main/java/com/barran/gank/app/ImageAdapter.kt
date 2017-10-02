package com.barran.gank.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener

import com.squareup.picasso.Picasso

import shivam.developer.featuredrecyclerview.FeatureRecyclerViewAdapter


/**
 * 展示图片的adapter[FeaturedRecyclerView][shivam.developer.featuredrecyclerview.FeaturedRecyclerView]
 *
 * @author barran_tan
 */
class ImageAdapter(private val context: Context, private val imageList: List<String>, private var itemClickListener: RecyclerViewItemClickListener?) : FeatureRecyclerViewAdapter<ImageAdapter.CustomRecyclerViewHolder>() {
    private val images = IntArray(5)

    override fun onCreateFeaturedViewHolder(parent: ViewGroup, viewType: Int): CustomRecyclerViewHolder {
        return CustomRecyclerViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(0, parent, false), itemClickListener)
    }

    override fun onBindFeaturedViewHolder(holder: CustomRecyclerViewHolder, position: Int) {
        Picasso.with(context)
                .load(images[position % 4]).into(holder.ivBackground)
        holder.tvHeading!!.text = imageList[position]
    }

    override fun getFeaturedItemsCount(): Int {
        return imageList.size
    }

    override fun onSmallItemResize(holder: CustomRecyclerViewHolder, position: Int, offset: Float) {
        holder.tvHeading!!.alpha = offset / 100f
    }

    override fun onBigItemResize(holder: CustomRecyclerViewHolder, position: Int, offset: Float) {
        holder.tvHeading!!.alpha = offset / 100f
    }

    class CustomRecyclerViewHolder(itemView: View, clickListener: RecyclerViewItemClickListener?) : BaseRecyclerHolder(itemView, clickListener) {

        internal var ivBackground: ImageView? = null
        internal var tvHeading: TextView? = null
    }
}
