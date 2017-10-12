package com.barran.gank.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.barran.gank.R
import com.barran.gank.libs.recycler.BaseRecyclerHolder
import com.barran.gank.libs.recycler.RecyclerViewItemClickListener

import com.squareup.picasso.Picasso

import shivam.developer.featuredrecyclerview.FeatureRecyclerViewAdapter


/**
 * 展示图片的adapter[FeaturedRecyclerView][shivam.developer.featuredrecyclerview.FeaturedRecyclerView]
 *
 * @author tanwei
 */
class ImageAdapter(private val context: Context, private val imageList: List<String>, private var itemClickListener: RecyclerViewItemClickListener?) : FeatureRecyclerViewAdapter<ImageAdapter.CustomRecyclerViewHolder>() {

    override fun onCreateFeaturedViewHolder(parent: ViewGroup, viewType: Int): CustomRecyclerViewHolder {
        return CustomRecyclerViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_image, parent, false), itemClickListener)
    }

    override fun onBindFeaturedViewHolder(holder: CustomRecyclerViewHolder, position: Int) {
        Picasso.with(context)
                .load(imageList[position]).into(holder.image)
//        holder.title!!.text = imageList[position]
    }

    override fun getFeaturedItemsCount(): Int {
        return imageList.size
    }

    override fun onSmallItemResize(holder: CustomRecyclerViewHolder, position: Int, offset: Float) {
//        holder.title!!.alpha = offset / 100f
    }

    override fun onBigItemResize(holder: CustomRecyclerViewHolder, position: Int, offset: Float) {
//        holder.title!!.alpha = offset / 100f
    }

    class CustomRecyclerViewHolder(itemView: View, clickListener: RecyclerViewItemClickListener?) : BaseRecyclerHolder(itemView, clickListener) {

        internal var image: ImageView? = null
//        internal var title: TextView? = null

        init {
            image = itemView.findViewById(R.id.item_image_icon) as ImageView
//            title = itemView.findViewById(R.id.item_image_title) as TextView
        }
    }
}
