package com.sheng.wang.common.utils

import java.util.*

/**
 * 定时器封装
 */
class WaitTimerTaskImpl {
    private var m: Long = 1000 //计时，间隔时间，单位毫秒，默认1秒
    private var timer: Timer? = null
    private var now: Long = 0 //当前时间

    /**
     * 设置计时间隔时间
     *
     * @param m 单位毫秒，默认1秒
     */
    fun setDelay(m: Long) {
        this.m = m
    }

    /**
     * 开始计时
     */
    fun start(timerTask: TimerTask?) {
        stop()
        timer = Timer()
        timer?.schedule(timerTask, m, m)
    }

    /**
     * 开始计时
     */
    fun start(onTimerTaskListener: OnTimerTaskListener?) {
        stop()
        now = 0
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                now += m
                onTimerTaskListener?.onRun(now, (now / 1000).toInt())
            }
        }, m, m)
    }

    /**
     * 结束计时
     */
    fun stop() {
        now = 0
        if (timer != null) {
            timer?.cancel()
            timer?.purge()
            timer = null
        }
    }

    /**
     * 重置时间
     */
    fun resetTime() {
        now = 0
    }

    /**
     * 是否正在计时
     */
    fun isTimer(): Boolean {
        return timer != null
    }

    interface OnTimerTaskListener {
        /**
         * 任务运行,子线程
         *
         * @param nowTime 当前运行时间 毫秒
         * @param time    当前运行时间 秒
         */
        fun onRun(nowTime: Long, time: Int)
    }
}