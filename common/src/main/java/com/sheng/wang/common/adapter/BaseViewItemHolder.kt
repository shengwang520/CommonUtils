package com.sheng.wang.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 * 上层holder
 */
abstract class BaseViewItemHolder<M>(var mView: View) {
    var context: Context = mView.context
    var mPosition = 0

    constructor(parent: ViewGroup?, @LayoutRes res: Int) : this(
        LayoutInflater.from(parent!!.context).inflate(res, parent, false)
    )

    open fun setData(data: M) {}
    open fun setPosition(position: Int) {
        mPosition = position
    }

    protected fun <T : View?> findViewById(@IdRes id: Int): T {
        return mView.findViewById(id)
    }
}