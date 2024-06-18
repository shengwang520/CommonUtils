package com.sheng.wang.common.base

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.sheng.wang.common.permission.onPermissionDenied
import com.sheng.wang.common.permission.onPermissionGranted
import com.sheng.wang.common.permission.registerPermissionLaunch
import com.sheng.wang.common.permission.registerPermissionsLaunch

/**
 * Activity 基类
 */
abstract class BaseActivity : AppCompatActivity() {
    private var currentFragment: Fragment? = null //当前显示的fragment

    /**
     * 获取activity 实例
     */
    val activity: BaseActivity
        get() = this

    /**
     * 申请单个权限
     */
    val permissionLauncher = registerPermissionLaunch({ onPermissionGranted?.invoke() }, { onPermissionDenied?.invoke() })

    /**
     * 申请多个权限
     */
    val permissionsLauncher = registerPermissionsLaunch({ onPermissionGranted?.invoke() }, { onPermissionDenied?.invoke() })

    /**
     * 返回键代理
     */
    private val backDispatcher by lazy {
        onBackPressedDispatcher
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBackDispatcher {
            onBackDispatcher()
        }
        initBindingView()
        initView()
        initListener()
        initViewModel()
    }

    /**
     * 初始化布局绑定控制器
     */
    protected abstract fun initBindingView()

    /**
     * 初始化布局
     */
    protected open fun initView() {}

    /**
     * 初始化事件
     */
    protected open fun initListener() {}

    /**
     * 初始化viewModel
     */
    protected open fun initViewModel() {}

    /**
     * 绑定toolbar
     * @param iconId 图片id
     */
    protected fun bindToolbar(toolbar: Toolbar?, iconId: Int) {
        activity.setSupportActionBar(toolbar) //toolbar与Activity绑定。
        val bar = activity.supportActionBar
        if (bar != null) {
            bar.setDisplayShowTitleEnabled(false)
            bar.setDisplayHomeAsUpEnabled(true) //显示toolbar左上角的返回按钮。
            bar.setHomeAsUpIndicator(iconId) //给左上角返回按钮设置图片。
            bar.elevation = 0f //设置阴影，高版本才能看见。
        }
    }

    /**
     * 注册返回键触发回掉
     * @param bloc 返回true 表示拦截 false不拦截
     */
    private fun registerBackDispatcher(bloc: () -> Boolean) {
        backDispatcher.addCallback(this) {
            if (!bloc()) {
                finish()
            }
        }
    }

    /**
     * 返回键拦截
     * @return true拦截返回键 false不拦截
     */
    open fun onBackDispatcher(): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (!onBackDispatcher()) {
                finish()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * fragment 切换显示
     * @param tag 标志
     */
    fun switchFragment(containerViewId: Int, targetFragment: Fragment?, tag: String? = null) {
        if (targetFragment == null) return
        val transaction = supportFragmentManager.beginTransaction()
        if (!targetFragment.isAdded && supportFragmentManager.findFragmentByTag(tag) == null) {
            currentFragment?.let {
                transaction.hide(it)
            }
            transaction.add(containerViewId, targetFragment, tag).commitNow()
        } else {
            currentFragment?.let {
                transaction.hide(it)
            }
            transaction.show(targetFragment).commitNow()
        }
        currentFragment = targetFragment
    }

    companion object {
        const val RESULT_DATA = "result_intent_data" ///数据返回key
        const val DATA_KEY = "intent_data_key" //基本请求参数进入key
        const val DATA_KEY_TWO = "intent_data_key_two" //基本请求参数进入key2
        const val DATA_KEY_THREE = "intent_data_key_three" //基本请求参数进入key3
        const val DATA_KEY_FOUR = "intent_data_key_four" //基本请求参数进入key4
    }

}