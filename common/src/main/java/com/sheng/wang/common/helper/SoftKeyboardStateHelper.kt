package com.sheng.wang.common.helper

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import java.util.*

/**
 * 键盘监听
 */
class SoftKeyboardStateHelper @JvmOverloads constructor(
    private val activityRootView: View, private var isSoftKeyboardOpened: Boolean = false
) : OnGlobalLayoutListener {
    private val listeners: MutableList<SoftKeyboardStateListener> = LinkedList()

    /**
     * Default value is zero (0)
     *
     * @return last saved keyboard height in px
     */
    private var lastSoftKeyboardHeightInPx = 0

    override fun onGlobalLayout() {
        val r = Rect()
        //r will be populated with the coordinates of your view that area still visible.
        activityRootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = activityRootView.rootView.height - (r.bottom - r.top)
//        Logger.d("key height:$heightDiff")
        if (!isSoftKeyboardOpened && heightDiff > 300) { // if more than 300 pixels, its probably a keyboard...
            isSoftKeyboardOpened = true
            notifyOnSoftKeyboardOpened(heightDiff)
        } else if (isSoftKeyboardOpened && heightDiff < 300) {
            isSoftKeyboardOpened = false
            notifyOnSoftKeyboardClosed()
        }
    }

    fun addSoftKeyboardStateListener(listener: SoftKeyboardStateListener) {
        listeners.add(listener)
    }

    fun removeSoftKeyboardStateListener(listener: SoftKeyboardStateListener) {
        listeners.remove(listener)
    }

    private fun notifyOnSoftKeyboardOpened(keyboardHeightInPx: Int) {
        lastSoftKeyboardHeightInPx = keyboardHeightInPx
        for (listener in listeners) {
            listener.onSoftKeyboardOpened(keyboardHeightInPx)
        }
    }

    private fun notifyOnSoftKeyboardClosed() {
        for (listener in listeners) {
            listener.onSoftKeyboardClosed()
        }
    }

    interface SoftKeyboardStateListener {
        /**
         * 键盘打开
         */
        fun onSoftKeyboardOpened(keyboardHeightInPx: Int)

        /**
         * 键盘关闭
         */
        fun onSoftKeyboardClosed()
    }

    init {
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }
}