package com.example.itsbubble

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class ResizeHandleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    enum class Corner { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }

    interface OnDragListener {
        fun onDrag(corner: Corner, dx: Int, dy: Int)
        fun onDragEnd()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFFFFF")
        style = Paint.Style.FILL
    }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#555555")
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private var cornerSize = 24f
    private var activeCorner: Corner? = null
    private var startX = 0f
    private var startY = 0f
    private var dragListener: OnDragListener? = null

    fun setOnDragListener(listener: OnDragListener) {
        dragListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val cs = cornerSize
        val corners = listOf(
            Corner.TOP_LEFT to (0f to 0f),
            Corner.TOP_RIGHT to (w - cs to 0f),
            Corner.BOTTOM_LEFT to (0f to h - cs),
            Corner.BOTTOM_RIGHT to (w - cs to h - cs)
        )
        for ((_, pos) in corners) {
            canvas.drawRect(pos.first, pos.second, pos.first + cs, pos.second + cs, paint)
            canvas.drawRect(pos.first, pos.second, pos.first + cs, pos.second + cs, strokePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                activeCorner = findCorner(event.x, event.y)
                startX = event.x
                startY = event.y
                return activeCorner != null
            }
            MotionEvent.ACTION_MOVE -> {
                activeCorner?.let { corner ->
                    val dx = (event.x - startX).toInt()
                    val dy = (event.y - startY).toInt()
                    dragListener?.onDrag(corner, dx, dy)
                    startX = event.x
                    startY = event.y
                }
                return activeCorner != null
            }
            MotionEvent.ACTION_UP -> {
                dragListener?.onDragEnd()
                activeCorner = null
                return true
            }
        }
        return false
    }

    private fun findCorner(x: Float, y: Float): Corner? {
        val w = width.toFloat()
        val h = height.toFloat()
        val cs = cornerSize
        val threshold = cs * 1.5f
        return when {
            abs(x) < threshold && abs(y) < threshold -> Corner.TOP_LEFT
            abs(x - w) < threshold && abs(y) < threshold -> Corner.TOP_RIGHT
            abs(x) < threshold && abs(y - h) < threshold -> Corner.BOTTOM_LEFT
            abs(x - w) < threshold && abs(y - h) < threshold -> Corner.BOTTOM_RIGHT
            else -> null
        }
    }
}
