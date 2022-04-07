package com.sheng.wang.common.widget

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView

/**
 * 适配5.0+ webView 问题
 */
class CWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    WebView(getFixedContext(context), attrs, defStyleAttr) {
    companion object {
        fun getFixedContext(context: Context): Context {
            return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                // Android Lollipop 5.0 & 5.1
                context.createConfigurationContext(Configuration())
            } else context
        }
    }
}