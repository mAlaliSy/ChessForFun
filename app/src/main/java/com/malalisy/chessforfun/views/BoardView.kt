package com.malalisy.chessforfun.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.malalisy.chessforfun.R
import com.malalisy.chessforfun.views.GameView.Companion.DEFAULT_DARK_COLOR
import com.malalisy.chessforfun.views.GameView.Companion.DEFAULT_LIGHT_COLOR
import kotlin.math.log
import kotlin.math.min

class BoardView : View {

    var size: Int = 0
        set(size) {
            field = size
            blockSize = size / 8
        }
    private var blockSize = 0

    var lightColor: Int = 0
        set(value) {
            field = value
            try {
                lightPaint.color = lightColor
            } catch (e: Exception) {
            }
        }
    var darkColor: Int = 0
        set(value) {
            field = value
            try {
                darkPaint.color = darkColor
            } catch (e: Exception) {
            }
        }

    private lateinit var lightPaint: Paint
    private lateinit var darkPaint: Paint
    private lateinit var blockRect: Rect

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init()
    }

    constructor(context: Context?, lightColor: Int, darkColor: Int) : super(context) {
        this.lightColor = lightColor
        this.darkColor = darkColor

        init()
    }


    private fun init() {
        lightPaint = Paint()
        lightPaint.color = lightColor

        darkPaint = Paint()
        darkPaint.color = darkColor

        blockRect = Rect()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = min(measuredWidth, measuredHeight)
        blockSize = size / 8

        setMeasuredDimension(size, size)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null)
            return
        blockRect.set(0, 0, size, size)
        canvas.drawRect(blockRect, lightPaint)


        var shift = 0
        for (y in 0..7) {
            for (x in 0..7 step 2) {
                blockRect.set((x + shift) * blockSize, y * blockSize, (x + 1 + shift) * blockSize, (y + 1) * blockSize)
                canvas.drawRect(blockRect, darkPaint)
            }
            shift = if (shift == 1) 0 else 1
        }

    }


}