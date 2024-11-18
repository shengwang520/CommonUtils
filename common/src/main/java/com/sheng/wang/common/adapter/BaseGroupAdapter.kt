package com.sheng.wang.common.adapter

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.sheng.wang.common.utils.MetricsUtils

/**
 * 目前只支持LinearLayout数据适配
 */
abstract class BaseGroupAdapter<T> @JvmOverloads protected constructor(
    viewGroup: LinearLayout, num: Int = 1
) {
    private val mViewGroup: LinearLayout
    private val mContext: Context
    private var mOffset = 0f
    private val mData: MutableList<T>
    private var mNum: Int //默认为每行1条数据
    private var mGravity = Gravity.START
    private var isResetWidth = false //是否需要重新设置宽度
    private var dividerId = 0 //分割线
    private var beginning = true//分割线是否显示在开始
    private var sizeNum: Int //每行按照多少数量计算
    var selectorHolder: BaseViewItemHolder<T>? = null
    private var mOnViewSelectedListener: OnViewSelectedListener? = null

    /**
     * 是否需要重新设置宽度,再num=1时有效,默认不需要
     */
    fun setResetWidth(resetWidth: Boolean) {
        isResetWidth = resetWidth
    }

    /**
     * 设置每一行的个数
     */
    fun setNum(num: Int) {
        mNum = num
        sizeNum = num
    }

    /**
     * 设置每一行
     */
    fun setSizeNum(sizeNum: Int) {
        this.sizeNum = sizeNum
    }

    /**
     * 设置布局内容显示位置
     */
    fun setGravity(gravity: Int) {
        mGravity = gravity
    }

    /**
     * 设置分割线
     */
    fun setDividerId(dividerId: Int, beginning: Boolean = true) {
        this.dividerId = dividerId
        this.beginning = beginning
    }

    /**
     * 设置偏移量(dp)
     */
    fun setOffset(offset: Float) {
        mOffset = offset
    }

    fun add(t: T) {
        mData.add(t)
    }

    fun addAll(data: List<T>?) {
        mData.addAll(data!!)
    }

    fun clear() {
        mData.clear()
    }

    /**
     * 刷新界面
     */
    fun notifyDataSetChanged() {
        changFrameLayoutViews(mViewGroup)
    }

    private fun changFrameLayoutViews(linearLayout: LinearLayout) {
        linearLayout.removeAllViews()
        if (count > 0) {
            if (mNum > 1) {
                var i = 0
                val size = count
                while (i < size) {
                    val itemParent = LinearLayout(mContext)
                    itemParent.orientation = LinearLayout.HORIZONTAL
                    itemParent.gravity = mGravity
                    if (dividerId > 0) {
                        itemParent.dividerDrawable = ContextCompat.getDrawable(mContext, dividerId)
                        itemParent.showDividers = if (beginning) (LinearLayout.SHOW_DIVIDER_MIDDLE or LinearLayout.SHOW_DIVIDER_BEGINNING)
                        else LinearLayout.SHOW_DIVIDER_MIDDLE
                    }
                    val itemNum = mNum + i
                    var j = i
                    val num = itemNum.coerceAtMost(size)
                    while (j < num) {
                        val holder = onCreateViewHolder(mViewGroup)
                        onBindViewHolder(holder, j)
                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        params.width = MetricsUtils.getWidth(mContext, mOffset, sizeNum)
                        itemParent.addView(holder.mView, params)
                        j++
                    }
                    linearLayout.addView(itemParent)
                    i += mNum
                }
            } else {
                var i = 0
                val size = count
                while (i < size) {
                    val holder = onCreateViewHolder(mViewGroup)
                    onBindViewHolder(holder, i)
                    if (isResetWidth) {
                        val itemParent = LinearLayout(mContext)
                        itemParent.orientation = LinearLayout.HORIZONTAL
                        itemParent.gravity = mGravity
                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        params.width = MetricsUtils.getWidth(mContext, mOffset, mNum)
                        itemParent.addView(holder.mView, params)
                        linearLayout.addView(itemParent)
                    } else {
                        linearLayout.addView(holder.mView)
                    }
                    i++
                }
            }
        }
    }

    val count: Int
        get() = mData.size

    fun getItem(position: Int): T {
        return mData[position]
    }

    fun setOnViewSelectedListener(onViewSelectedListener: OnViewSelectedListener?) {
        mOnViewSelectedListener = onViewSelectedListener
    }

    abstract fun onCreateViewHolder(parent: ViewGroup?): BaseViewItemHolder<T>
    private fun onBindViewHolder(holder: BaseViewItemHolder<T>, position: Int) {
        holder.mView.setOnClickListener {
            if (selectorHolder != null && selectorHolder === holder) {
                mOnViewSelectedListener?.onViewReselected(holder)
            } else {
                mOnViewSelectedListener?.onViewSelected(holder)
                if (selectorHolder != null) {
                    mOnViewSelectedListener?.onViewUnselected(selectorHolder)
                }
                selectorHolder = holder
            }
        }
        holder.setPosition(position)
        holder.setData(getItem(position))
    }

    interface OnViewSelectedListener {
        fun onViewSelected(holder: BaseViewItemHolder<*>?)
        fun onViewUnselected(holder: BaseViewItemHolder<*>?)
        fun onViewReselected(holder: BaseViewItemHolder<*>?)
    }

    init {
        viewGroup.orientation = LinearLayout.VERTICAL
        mViewGroup = viewGroup
        mNum = num
        sizeNum = num
        mContext = viewGroup.context
        mData = ArrayList()
    }
}