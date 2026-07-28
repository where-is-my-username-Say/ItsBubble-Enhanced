package com.example.itsbubble.ui.zoomable

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.max
import kotlin.math.min

class ZoomableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val matrix = Matrix()
    private val savedMatrix = Matrix()
    private val startPoint = PointF()
    private val midPoint = PointF()
    private var mode = NONE

    private var scaleGestureDetector: ScaleGestureDetector
    private var gestureDetector: GestureDetector

    private var saveScale = 1f
    private var minScale = 1f
    private var maxScale = 5f

    private var viewWidth = 0
    private var viewHeight = 0
    private var origWidth = 0f
    private var origHeight = 0f
    private var right = 0f
    private var bottom = 0f

    init {
        scaleType = ScaleType.MATRIX
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (drawable != null) {
            fitToScreen()
        }
    }

    private fun fitToScreen() {
        val drawable = drawable ?: return
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        val scaleX = viewWidth.toFloat() / drawableWidth
        val scaleY = viewHeight.toFloat() / drawableHeight
        val scale = min(scaleX, scaleY)
        saveScale = scale
        minScale = scale
        matrix.setScale(scale, scale)
        val redundantYSpace = viewHeight.toFloat() - (scale * drawableHeight)
        val redundantXSpace = viewWidth.toFloat() - (scale * drawableWidth)
        origWidth = viewWidth - 2 * redundantXSpace
        origHeight = viewHeight - 2 * redundantYSpace
        right = viewWidth * saveScale - viewWidth
        bottom = viewHeight * saveScale - viewHeight
        matrix.postTranslate(redundantXSpace / 2, redundantYSpace / 2)
        imageMatrix = matrix
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        val point = PointF(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                savedMatrix.set(matrix)
                startPoint.set(point)
                mode = DRAG
            }
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                matrix.set(savedMatrix)
                val dx = point.x - startPoint.x
                val dy = point.y - startPoint.y
                val fixTransX = getFixTrans(dx, viewWidth.toFloat(), origWidth * saveScale)
                val fixTransY = getFixTrans(dy, viewHeight.toFloat(), origHeight * saveScale)
                matrix.postTranslate(fixTransX, fixTransY)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> mode = NONE
        }
        imageMatrix = matrix
        return true
    }

    private fun getFixTrans(trans: Float, viewSize: Float, contentSize: Float): Float {
        val minTrans: Float
        val maxTrans: Float
        if (contentSize <= viewSize) {
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else {
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }
        return when {
            trans < minTrans -> -trans + minTrans
            trans > maxTrans -> -trans + maxTrans
            else -> 0f
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor = detector.scaleFactor
            val origScale = saveScale
            saveScale *= mScaleFactor
            if (saveScale > maxScale) {
                saveScale = maxScale
                mScaleFactor = maxScale / origScale
            } else if (saveScale < minScale) {
                saveScale = minScale
                mScaleFactor = minScale / origScale
            }
            right = viewWidth * saveScale - viewWidth
            bottom = viewHeight * saveScale - viewHeight
            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight) {
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2f, viewHeight / 2f)
            } else {
                matrix.postScale(mScaleFactor, mScaleFactor, detector.focusX, detector.focusY)
            }
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            if (saveScale == minScale) {
                saveScale = min(maxScale, 2.5f)
                matrix.setScale(saveScale, saveScale, viewWidth / 2f, viewHeight / 2f)
            } else {
                fitToScreen()
            }
            imageMatrix = matrix
            return true
        }
    }

    companion object {
        private const val NONE = 0
        private const val DRAG = 1
        private const val ZOOM = 2
    }
}
