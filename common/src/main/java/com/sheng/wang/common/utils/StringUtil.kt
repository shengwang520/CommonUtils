package com.sheng.wang.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.View

/**
 * 字符串拼接
 */
object StringUtil {
    /**
     * 设置图文混排
     */
    fun getString2Value(context: Context, textStrings: List<TextString>): SpannableStringBuilder {
        val stringBuilder = SpannableStringBuilder()
        for (textString in textStrings) {
            if (textString.icon > 0) {
                stringBuilder.append(getImageSpan(context, textString.icon, textString.iconWith, textString.iconHeight))
            } else {
                val spannableString = SpannableString(textString.text)
                if (textString.onClickListener != null) {
                    //设置部分文字点击事件
                    val clickableSpan: ClickableSpan = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            textString.onClickListener?.onClick(widget)
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.color = Color.parseColor(textString.color)
                            ds.isUnderlineText = textString.isLineText
                        }
                    }
                    spannableString.setSpan(
                        clickableSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    val colorSpan = ForegroundColorSpan(Color.parseColor(textString.color))
                    spannableString.setSpan(
                        colorSpan, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                stringBuilder.append(spannableString)
            }
        }
        return stringBuilder
    }

    /**
     * 设置文字颜色，大小变化
     * @param text 完整内容
     * @param content 需要变化的内容
     * @param color 需要变化的颜色
     * @param size 需要变化的大小
     */
    fun getStringAndColor(text: String?, content: List<String>?, color: String, size: Int): SpannableStringBuilder {
        val stringBuilder = SpannableStringBuilder(text)
        if (content?.isNotEmpty() == true) {
            for (str in content) {
                val index = text?.indexOf(str) ?: 0
                val colorSpan = ForegroundColorSpan(Color.parseColor(color))
                stringBuilder.setSpan(colorSpan, index, index + str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val sizeSpan = AbsoluteSizeSpan(size, true)
                stringBuilder.setSpan(sizeSpan, index, index + str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return stringBuilder
    }

    /**
     * 设置文字颜色，大小变化,点击事件
     * @param text 完整内容
     * @param content 需要变化的内容
     * @param color 需要变化的颜色
     * @param size 需要变化的大小
     * @param onClickText 给变化的颜色添加点击效果
     */
    fun getStringAndColor(
        text: String?,
        content: String?,
        color: String,
        size: Int = 0,
        onClickText: (() -> Unit)? = null
    ): SpannableStringBuilder {
        val stringBuilder = SpannableStringBuilder(text)
        if (!content.isNullOrEmpty()) {
            val index = text?.indexOf(content) ?: 0
            val colorSpan = ForegroundColorSpan(Color.parseColor(color))
            stringBuilder.setSpan(colorSpan, index, index + content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (size > 0) {
                val sizeSpan = AbsoluteSizeSpan(size, true)
                stringBuilder.setSpan(sizeSpan, index, index + content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            if (onClickText != null) {
                val clickSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onClickText()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = Color.parseColor(color)
                        ds.isUnderlineText = false
                    }
                }
                stringBuilder.setSpan(clickSpan, index, index + content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return stringBuilder
    }

    /**
     * 获取图片标签
     */
    private fun getImageSpan(context: Context, resId: Int, iconWith: Int = 0, iconHeight: Int = 0): SpannableString {
        val spannableString = SpannableString("0")
        var bitmap = BitmapFactory.decodeResource(context.resources, resId)
        if (iconWith != 0 && iconHeight != 0) {
            bitmap = Bitmap.createScaledBitmap(bitmap, iconWith, iconHeight, true)
        }
        val imageSpan = CenterAlignImageSpan(context, bitmap)
        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    class TextString {
        var text: String? = null
        var color: String? = null
        var icon = 0
        var onClickListener: View.OnClickListener? = null

        /**
         * 文字是否有下划线
         */
        var isLineText: Boolean = false

        var iconWith = 0
        var iconHeight = 0

        constructor(icon: Int) {
            this.icon = icon
        }

        constructor(icon: Int, iconWith: Int = 0, iconHeight: Int = 0) {
            this.icon = icon
            this.iconWith = iconWith
            this.iconHeight = iconHeight
        }

        @JvmOverloads
        constructor(
            text: String?, color: String?, onClickListener: View.OnClickListener? = null, isLineText: Boolean = false
        ) {
            this.text = text
            this.color = color
            this.onClickListener = onClickListener
            this.isLineText = isLineText
        }

    }

    /**
     * 图片居中
     */
    class CenterAlignImageSpan(context: Context, bitmap: Bitmap) : ImageSpan(context, bitmap) {

        override fun draw(
            canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint
        ) {
            val b = drawable
            val fm = paint.fontMetricsInt
            val transY = (y + fm.descent + y + fm.ascent) / 2 - b.bounds.bottom / 2 //计算y方向的位移
            canvas.save()
            canvas.translate(x, transY.toFloat()) //绘制图片位移一段距离
            b.draw(canvas)
            canvas.restore()
        }
    }
}