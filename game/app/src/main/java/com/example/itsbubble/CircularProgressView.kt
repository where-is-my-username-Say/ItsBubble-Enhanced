package com.example.itsbubble

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4CAF50")
        style = Paint.Style.STROKE
        strokeWidth = 8f
        strokeCap = Paint.Cap.ROUND
    }
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#333333")
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }
    private val rect = RectF()
    private var animator: ValueAnimator? = null
    private var sweepAngle = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val padding = 10f
        rect.set(padding, padding, width - padding, height - padding)
        canvas.drawArc(rect, 0f, 360f, false, bgPaint)
        canvas.drawArc(rect, -90f, sweepAngle, false, paint)
    }

    fun startProgress() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 1200
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                sweepAngle = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    fun cancelProgress() {
        animator?.cancel()
        sweepAngle = 0f
        invalidate()
    }
}
