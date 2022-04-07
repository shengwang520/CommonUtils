package com.sheng.wang.common.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.*
import android.text.Html.ImageGetter
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.View
import androidx.core.content.res.ResourcesCompat

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
                stringBuilder.append(getImageSpan(context, textString.icon))
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
     * 获取图片标签
     */
    private fun getImageSpan(context: Context, resId: Int): SpannableString {
        val hotSpan = Html.fromHtml("<img src='$resId'/> ", ImageGetter { source ->
            if (!TextUtils.isEmpty(source)) {
                val id = source.toInt()
                //根据id从资源文件中获取图片对象
                val d = ResourcesCompat.getDrawable(
                    context.resources, id, null
                )
                d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
                return@ImageGetter d!!
            }
            null
        }, null)
        val spannableString = SpannableString(hotSpan)
        val imageSpan = CenterAlignImageSpan(context, resId)
        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
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

        constructor(icon: Int) {
            this.icon = icon
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
    class CenterAlignImageSpan : ImageSpan {
        constructor(drawable: Drawable) : super(drawable)
        constructor(context: Context, resourceId: Int) : super(context, resourceId)

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