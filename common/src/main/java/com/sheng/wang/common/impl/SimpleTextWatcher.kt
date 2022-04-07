package com.sheng.wang.common.impl

import android.text.Editable
import android.text.TextWatcher

/**
 * 输入框简单实现
 */
open class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {}
}