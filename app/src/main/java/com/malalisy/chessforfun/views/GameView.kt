package com.malalisy.chessforfun.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.malalisy.chessforfun.R
import com.malalisy.chessforfun.pojos.Move
import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.Point
import com.malalisy.chessforfun.pojos.pieces.*
import com.malalisy.chessforfun.utils.convertPoint
import com.malalisy.chessforfun.utils.drawCenter
import com.malalisy.chessforfun.utils.getIconTypeFace
import com.malalisy.chessforfun.utils.getPixelsFromDP
import kotlin.math.min

class GameView : View {


    var board: Array<Array<Piece?>>? = null
    var playerColor: PlayerColor? = null
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

    private val PIECE_FONT_SIZE: Float = getPixelsFromDP(42f).toFloat()

    var boardListener: BoardListener? = null
    var touchedPoint: Point? = null

    var acceptMoves = true

    var highlightedAvailableMoves = ArrayList<Point>()
    private lateinit var movesHighlighterPaint: Paint


    companion object {
        val DEFAULT_LIGHT_COLOR = Color.parseColor("#d9ddc1")
        val DEFAULT_DARK_COLOR = Color.parseColor("#669C55")
    }

    constructor(context: Context, board: Array<Array<Piece?>>, playerColor: com.malalisy.chessforfun.pojos.PlayerColor) : super(context) {
        init(DEFAULT_LIGHT_COLOR, DEFAULT_DARK_COLOR)
        this.board = board
        this.playerColor = playerColor
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
        blackPaint.textSize = PIECE_FONT_SIZE
        blackPaint.isAntiAlias = true
        blackPaint.isSubpixelText = true
        blackPaint.typeface = typeface
        blackPaint.style = Paint.Style.FILL

        whitePaint = Paint()
        whitePaint.typeface = getIconTypeFace(context)
        whitePaint.color = Color.parseColor("#fcfcfc")
        whitePaint.textSize = PIECE_FONT_SIZE
        whitePaint.isAntiAlias = true
        whitePaint.isSubpixelText = true
        whitePaint.typeface = typeface
        whitePaint.style = Paint.Style.FILL

        movesHighlighterPaint = Paint()
        movesHighlighterPaint.color = Color.BLACK
        movesHighlighterPaint.alpha = 90


        rect = Rect()

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

        if (board == null || playerColor == null)
            return

        /*
        * Highlight the avaliable moves
        * */

        for (hPoint in highlightedAvailableMoves) {
            val point = convertPoint(hPoint, playerColor!!)
            canvas.drawCircle((point.x + 0.5f) * blockSize, (point.y + 0.5f) * blockSize, blockSize / 5f, movesHighlighterPaint)
        }

        var icon: String
        for (y in 0..7) {
            for (x in 0..7) {

                val piece = if (playerColor == com.malalisy.chessforfun.pojos.PlayerColor.WHITE) {
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (boardListener == null)
            return true

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {

                val point = getTouchedSquare(event.x.toInt(), event.y.toInt())
                val selectedPiece = board!![point.y][point.x] ?: return true

                if (selectedPiece.playerColor == playerColor) {
                    touchedPoint = point
                    boardView.highlightedSquares.clear()
                    boardView.highlightedSquares.add(touchedPoint!!)

                    if (acceptMoves) {
                        highlightedAvailableMoves.clear()
                        boardListener?.onSquareSelected(touchedPoint!!)
                    }
                }

            }

            MotionEvent.ACTION_UP -> {
                if (acceptMoves) {
                    var point = getTouchedSquare(event.x.toInt(), event.y.toInt())
                    if (touchedPoint != null && point != touchedPoint) {

                        /*
                        * Check if the move is valid move
                        * */
                        if (boardListener!!.onMoveSelected(point)) {
                            touchedPoint = null
                            boardView.highlightedSquares.add(point)
                            highlightedAvailableMoves.clear()
                        }
                    }
                }
            }

        }
        invalidate()

        return true
    }

    fun getTouchedSquare(x: Int, y: Int): Point {
        var newX = x / blockSize
        var newY = y / blockSize
        newX = if (playerColor == PlayerColor.WHITE) newX else 7 - newX
        newY = if (playerColor == PlayerColor.WHITE) 7 - newY else newY
        return Point(newX, newY)
    }


    fun setBoardLightColor(color: Int) {
        boardView.lightColor = color
    }

    fun setBoardDarkColor(color: Int) {
        boardView.darkColor = color
    }


    fun onOpponentPlayed(move: Move) {
        acceptMoves = true
        boardView.highlightedSquares.clear()
        boardView.highlightedSquares.add(move.from)
        boardView.highlightedSquares.add(move.to)
        invalidate()
    }

    fun highlightAvailableMoves(points: List<Point>) {
        highlightedAvailableMoves.addAll(points)
    }

    interface BoardListener {
        fun onSquareSelected(point: Point)
        fun onMoveSelected(to: Point): Boolean
        fun onMoveCanceled()

    }

}