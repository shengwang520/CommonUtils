package com.sheng.wang.common.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * 能自动滚动的viewPager
 */
class BannerViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    private val countDownTime = 24 * 60 * 60 * 1000L//1天的计时器
    private val countDownInterval = 3 * 1000L//3秒间隔

    private var count = 0//数量
    private var countdownTimer: CountDownTimer? = null//自动滚动计时器

    //指示器
    private var radioGroup: RadioGroup? = null
    private var radioButtons = ArrayList<RadioButton>()

    /**
     * 是否自动滚动，默认是
     */
    var isAutoScroll = true

    //页面滚动监听
    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            if (radioButtons.size > position) {
                radioButtons[position].isChecked = true
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }


    /**
     * 设置布局并绑定适配器
     */
    fun setupWithAdapter(views: List<View>) {
        this.count = views.size
        val adapter = ViewPagerAdapter(views)
        setAdapter(adapter)
        this.addOnPageChangeListener(onPageChangeListener)
    }

    /**
     * 绑定radioGroup
     * @param resId radiobutton 按钮状态图片
     * @suppress 需要先调用
     * @see setupWithAdapter
     */
    fun setupWithRadioGroup(radioGroup: RadioGroup?, resId: Int, with: Float = 8f, margin: Float = 4f) {
        this.radioGroup = radioGroup
        radioButtons.clear()
        radioButtons.addAll(createButtons(resId, with, margin))
        if (radioButtons.isNotEmpty()) {
            radioButtons[0].isChecked = true
        }

        if (count > 1) {
            radioGroup?.visibility = VISIBLE
        } else {
            radioGroup?.visibility = GONE
        }

        startCountdownTimer()
    }

    /**
     * 绑定radioGroup
     * @param radioButton 自定义radioButton
     * @suppress 需要先调用
     * @see setupWithAdapter
     */
    fun setupWithRadioGroup(radioGroup: RadioGroup?, radioButton: List<RadioButton>) {
        this.radioGroup = radioGroup
        this.radioButtons.clear()
        this.radioButtons.addAll(radioButton)
        if (radioButtons.isNotEmpty()) {
            radioButtons[0].isChecked = true
        }

        if (count > 1) {
            radioGroup?.visibility = VISIBLE
        } else {
            radioGroup?.visibility = GONE
        }

        startCountdownTimer()
    }

    /**
     * 动态创建按钮
     */
    private fun createButtons(
        resId: Int,
        size: Float = 8f,
        margin: Float = 4f
    ): List<RadioButton> {
        radioGroup?.removeAllViews()
        val radioButtons: MutableList<RadioButton> = ArrayList()
        val sizeW = dp2px(size)
        val marginS = dp2px(margin)
        for (i in 0 until count) {
            val radioButton = RadioButton(context)
            val params = RadioGroup.LayoutParams(sizeW, sizeW)
            params.setMargins(marginS, 0, marginS, 0)
            radioButton.buttonDrawable = ColorDrawable(Color.TRANSPARENT)
            radioButton.setBackgroundResource(resId)
            radioButton.setOnClickListener { }
            radioButtons.add(radioButton)
            radioGroup?.addView(radioButton, params)
        }
        return radioButtons
    }

    /**
     * 启动倒计时
     */
    private fun startCountdownTimer() {
        if (isAutoScroll && count > 1) {
            stopCountdownTimer()
            countdownTimer = object : CountDownTimer(countDownTime, countDownInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    if (countDownTime - millisUntilFinished >= countDownInterval) {//开始不滚动
                        val index = currentItem + 1
                        if (index == count) {
                            setCurrentItem(0, false)
                        } else {
                            currentItem = index
                        }
                    }
                }

                override fun onFinish() {
                    //计时器结束，重新开启
                    startCountdownTimer()
                }
            }
            countdownTimer?.start()
        } else {
            stopCountdownTimer()
        }
    }

    /**
     * 停止计时
     */
    private fun stopCountdownTimer() {
        countdownTimer?.cancel()
        countdownTimer = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        this.removeOnPageChangeListener(onPageChangeListener)
        stopCountdownTimer()
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return super.onTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 绑定页面声明周期，自动处理滚动逻辑
     */
    fun bindLifecycleObserver(owner: LifecycleOwner?) {
        owner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                resume()
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                pause()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                pause()
            }
        })
    }

    /**
     * 暂停
     */
    fun pause() {
        stopCountdownTimer()
    }

    /**
     * 重新唤醒
     */
    fun resume() {
        startCountdownTimer()
    }

    /**
     * dp转px
     */
    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    private class ViewPagerAdapter(private val views: List<View>) : PagerAdapter() {
        override fun getCount(): Int {
            return views.size
        }

        override fun isViewFromObject(view: View, o: Any): Boolean {
            return view === o
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view: View = views[position]
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(views[position])
        }
    }
}