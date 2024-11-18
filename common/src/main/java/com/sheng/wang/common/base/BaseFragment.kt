package com.sheng.wang.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Fragment 基类
 */
abstract class BaseFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initViewModel()
    }

    /**
     * 返回界面布局id
     */
    abstract fun createView(inflater: LayoutInflater, container: ViewGroup?): View?

    /**
     * 继承BaseFragment的碎片必须要实现的方法, 实现业务逻辑
     */
    protected abstract fun initView()

    /**
     * 初始化事件
     */
    protected open fun initListener() {}

    /**
     * 初始化viewModel
     */
    protected open fun initViewModel() {}

    companion object {
        const val DATA_KEY = "data_page" //页面page
    }
}