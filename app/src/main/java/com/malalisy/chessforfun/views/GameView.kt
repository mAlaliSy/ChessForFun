package com.malalisy.chessforfun.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.malalisy.chessforfun.R
import com.malalisy.chessforfun.pojos.pieces.*
import com.malalisy.chessforfun.utils.drawCenter
import com.malalisy.chessforfun.utils.getIconTypeFace
import com.malalisy.chessforfun.utils.getPixelsFromDP
import kotlin.math.min

class GameView : SurfaceView {


    var board: Array<Array<Piece?>>? = null
    var playerPlayerColor: com.malalisy.chessforfun.pojos.PlayerColor? = null
        set (value) {
            field = value
            boardView.playerColor = value
        }

    lateinit var boardView: BoardView

    private lateinit var whitePaint: Paint
    private lateinit var blackPaint: Paint

    var size = 0
        set(value) {
            field = value
            blockSize = size / 8
        }
    var blockSize = 0


    lateinit var rect: Rect

    private val FONT_SIZE: Float = getPixelsFromDP(42f).toFloat()

    companion object {
        val DEFAULT_LIGHT_COLOR = Color.parseColor("#d9ddc1")
        val DEFAULT_DARK_COLOR = Color.parseColor("#669C55")
    }

    constructor(context: Context, board: Array<Array<Piece?>>, playerColor: com.malalisy.chessforfun.pojos.PlayerColor) : super(context) {
        init(DEFAULT_LIGHT_COLOR, DEFAULT_DARK_COLOR)
        this.board = board
        this.playerPlayerColor = playerColor
        boardView.playerColor = playerColor
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        val typedArray = context.theme.obtainStyledAttributes(attributes, R.styleable.GameView, 0, 0)
        val lightColor = typedArray.getColor(R.styleable.GameView_lightColor, DEFAULT_LIGHT_COLOR)
        val darkColor = typedArray.getColor(R.styleable.GameView_darkColor, DEFAULT_DARK_COLOR)
        init(lightColor, darkColor)
    }

    private fun init(lightColor: Int, darkColor: Int) {
        boardView = BoardView(context, lightColor, darkColor)

        var typeface = getIconTypeFace(context)
        blackPaint = Paint()
        blackPaint.color = Color.parseColor("#393939")
        blackPaint.textSize = FONT_SIZE
        blackPaint.isAntiAlias = true
        blackPaint.isSubpixelText = true
        blackPaint.typeface = typeface
        blackPaint.style = Paint.Style.FILL


        whitePaint = Paint()
        whitePaint.typeface = getIconTypeFace(context)
        whitePaint.color = Color.parseColor("#fcfcfc")
        whitePaint.textSize = FONT_SIZE
        whitePaint.isAntiAlias = true
        whitePaint.isSubpixelText = true
        whitePaint.typeface = typeface
        whitePaint.style = Paint.Style.FILL



        rect = Rect()


        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder?) {}

            override fun surfaceCreated(p0: SurfaceHolder?) {
                val canvas = p0?.lockCanvas()
                if (canvas != null) {
                    draw(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }
            }

        })
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = min(measuredWidth, measuredHeight)

        boardView.size = size
        setMeasuredDimension(size, size)
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        boardView.draw(canvas)

        if (board == null || playerPlayerColor == null)
            return

        var icon: String
        for (y in 0..7) {
            for (x in 0..7) {

                val piece = if (playerPlayerColor == com.malalisy.chessforfun.pojos.PlayerColor.WHITE) {
                    board!![7 - y][x]
                } else board!![y][7 - x]

                if (piece == null)
                    continue

                icon = when (piece) {
                    is Pawn -> "\uf443"
                    is Knight -> "\uf441"
                    is Bishop -> "\uf43a"
                    is Rook -> "\uf447"
                    is Queen -> "\uf445"
                    is King -> "\uf43f"
                    else -> {
                        ""
                    }
                }

                drawCenter(canvas, y * blockSize + blockSize / 2f, x * blockSize + blockSize / 2f,
                        if (piece.playerColor == com.malalisy.chessforfun.pojos.PlayerColor.WHITE)
                            whitePaint
                        else blackPaint
                        , icon
                )

            }

        }


    }


    fun setBoardLightColor(color: Int) {
        boardView.lightColor = color
    }

    fun setBoardDarkColor(color: Int) {
        boardView.darkColor = color
    }


}