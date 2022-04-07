package com.sheng.wang.common.utils

import android.content.Context
import android.text.Selection
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * EditView相关设置
 */
object EditViewUtils {
    /**
     * 关闭键盘
     */
    fun closeInput(context: Context, edit: EditText) {
        val imm = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(edit.windowToken, 0)
    }

    /**
     * 弹出键盘
     */
    fun openInput(context: Context, edit: EditText) {
        edit.isFocusable = true
        edit.isFocusableInTouchMode = true
        edit.requestFocus()
        val imm = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.showSoftInput(edit, 0)
    }

    /**
     * 移动光标
     */
    fun moveGB2End(editText: EditText) {
        val text = editText.editableText
        Selection.setSelection(text, text.length)
    }
}