package com.malalisy.chessforfun.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.malalisy.chessforfun.R
import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.utils.drawCenter
import com.malalisy.chessforfun.utils.getPixelsFromDP
import com.malalisy.chessforfun.utils.getPixelsFromSP
import com.malalisy.chessforfun.views.GameView.Companion.DEFAULT_DARK_COLOR
import com.malalisy.chessforfun.views.GameView.Companion.DEFAULT_LIGHT_COLOR
import kotlin.math.log
import kotlin.math.min

class BoardView : View {

    val ALGERBRIC_FONT_SIZE = getPixelsFromSP(12f)
    val ALGEBRIC_PADDING = getPixelsFromDP(12f)

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
    private lateinit var algebricPaint: Paint

    private lateinit var blockRect: Rect
    var playerColor: PlayerColor? = null

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

        algebricPaint = Paint()
        algebricPaint.color = Color.parseColor("#393939")
        algebricPaint.textSize = ALGERBRIC_FONT_SIZE.toFloat()
        val rect = Rect()
        algebricPaint.getTextBounds("a", 0, 1, rect)
        algebricPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)





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


        var shift = if (playerColor == PlayerColor.WHITE) 1 else 0

        for (y in 0..7) {
            for (x in 0..7 step 2) {
                blockRect.set((x + shift) * blockSize, y * blockSize, (x + 1 + shift) * blockSize, (y + 1) * blockSize)
                canvas.drawRect(blockRect, darkPaint)
            }
            shift = if (shift == 1) 0 else 1
        }


        /*
        * Draw rows and columns names
        * */
        algebricPaint.color = if (playerColor == PlayerColor.WHITE) darkColor else lightColor

        for (i in 0..7) {
            drawCenter(canvas, i * blockSize + ALGEBRIC_PADDING / 2f, ALGEBRIC_PADDING / 2f, algebricPaint,
                    (if (playerColor == PlayerColor.WHITE) 8 - i else i + 1).toString()
            )

            algebricPaint.color = if (algebricPaint.color == darkColor) lightColor else darkColor
        }

        algebricPaint.color = if (playerColor == PlayerColor.WHITE) lightColor else darkColor
        val range = if (playerColor == PlayerColor.WHITE) 'a'..'h' else 'h' downTo 'a'
        var left = 0
        for (i in range) {
            drawCenter(canvas, size - ALGEBRIC_PADDING / 2f, (left + 1) * blockSize - ALGEBRIC_PADDING / 2f, algebricPaint, i.toString())
            algebricPaint.color = if (algebricPaint.color == darkColor) lightColor else darkColor
            left++
        }

    }


}