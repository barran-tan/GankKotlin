package com.barran.gank.libs.recycler

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.barran.gank.R
import com.barran.gank.app.App

/**
 * 分割线（竖直方向）
 *
 * Created by tanwei on 2017/10/11.
 */
class VerticalItemDecoration : RecyclerView.ItemDecoration() {

    private var dividerHeight: Int
    private val dividerPaint: Paint = Paint()
    private var includePadding = true// 是否包含item的padding区域

    private var showHeaderDivider: Boolean = false// 是否显示第一行的顶部分割线

    init {
        dividerPaint.color = App.appContext.resources.getColor(android.R.color.transparent)
        dividerHeight = App.appContext.resources.getDimensionPixelOffset(R.dimen.default_dimen_divider)
    }

    fun setDividerHeight(px: Int): VerticalItemDecoration {
        dividerHeight = px
        return this
    }

    fun setDividerColor(color: Int): VerticalItemDecoration {
        dividerPaint.color = color
        return this
    }

    fun setDividerIncludePadding(isInclude: Boolean): VerticalItemDecoration {
        includePadding = isInclude
        return this
    }

    fun showHeaderDivider(showHeaderDivider: Boolean): VerticalItemDecoration {
        this.showHeaderDivider = showHeaderDivider
        return this
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight
    }

    /***
     * 在item 的draw 之前调用
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val childCount = parent.childCount
        var left = parent.paddingLeft
        if (!includePadding) {
            left += parent.paddingLeft
        }
        var right = parent.width
        if (!includePadding) {
            right -= parent.paddingRight
        }

        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom.toFloat()
            val bottom = (view.bottom + dividerHeight).toFloat()
            c.drawRect(left.toFloat(), top, right.toFloat(), bottom, dividerPaint)
        }
    }

    /***
     * 在item 的draw 之后调用
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)

        if (showHeaderDivider && parent.childCount > 0) {
            var left = parent.paddingLeft
            if (!includePadding) {
                left += parent.paddingLeft
            }
            var right = parent.width
            if (!includePadding) {
                right -= parent.paddingRight
            }

            val view = parent.getChildAt(0)
            val top = view.top.toFloat()
            val bottom = top + dividerHeight
            c.drawRect(left.toFloat(), top, right.toFloat(), bottom, dividerPaint)
        }
    }
}