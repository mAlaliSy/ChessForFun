package com.malalisy.chessforfun.chess_engine.evaluation_functions

import com.malalisy.chessforfun.Color
import com.malalisy.chessforfun.pieces.*

class MaterialEvaluation : EvaluationFeature {

    private val PAWN_SQUARE_TABLE = arrayOf(
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(50, 50, 50, 50, 50, 50, 50, 50),
            arrayOf(10, 10, 20, 30, 30, 20, 10, 10),
            arrayOf(5, 5, 10, 25, 25, 10, 5, 5),
            arrayOf(0, 0, 0, 20, 20, 0, 0, 0),
            arrayOf(5, -5, -10, 0, 0, -10, -5, 5),
            arrayOf(5, 10, 10, -20, -20, 10, 10, 5),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0)
    )

    private val KNIGHT_SQUARE_TABLE = arrayOf(
            arrayOf(-50, -40, -30, -30, -30, -30, -40, -50),
            arrayOf(-40, -20, 0, 0, 0, 0, -20, -40),
            arrayOf(-30, 0, 10, 15, 15, 10, 0, -30),
            arrayOf(-30, 5, 15, 20, 20, 15, 5, -30),
            arrayOf(-30, 0, 15, 20, 20, 15, 0, -30),
            arrayOf(-30, 5, 10, 15, 15, 10, 5, -30),
            arrayOf(-40, -20, 0, 5, 5, 0, -20, -40),
            arrayOf(-50, -40, -30, -30, -30, -30, -40, -50)
    )

    val BISHOP_SQUARE_TABLE = arrayOf(
            arrayOf(-20, -10, -10, -10, -10, -10, -10, -20),
            arrayOf(-10, 0, 0, 0, 0, 0, 0, -10),
            arrayOf(-10, 0, 5, 10, 10, 5, 0, -10),
            arrayOf(-10, 5, 5, 10, 10, 5, 5, -10),
            arrayOf(-10, 0, 10, 10, 10, 10, 0, -10),
            arrayOf(-10, 10, 10, 10, 10, 10, 10, -10),
            arrayOf(-10, 5, 0, 0, 0, 0, 5, -10),
            arrayOf(-20, -10, -10, -10, -10, -10, -10, -20)
    )


    private val ROOK_SQUARE_TABLE = arrayOf(
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(5, 10, 10, 10, 10, 10, 10, 5),
            arrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
            arrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
            arrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
            arrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
            arrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
            arrayOf(0, 0, 0, 5, 5, 0, 0, 0)
    )


    val QUEEN_SQUARE_TABLE = arrayOf(
            arrayOf(-20, -10, -10, -5, -5, -10, -10, -20),
            arrayOf(-10, 0, 0, 0, 0, 0, 0, -10),
            arrayOf(-10, 0, 5, 5, 5, 5, 0, -10),
            arrayOf(-5, 0, 5, 5, 5, 5, 0, -5),
            arrayOf(0, 0, 5, 5, 5, 5, 0, -5),
            arrayOf(-10, 5, 5, 5, 5, 5, 0, -10),
            arrayOf(-10, 0, 5, 0, 0, 0, 0, -10),
            arrayOf(-20, -10, -10, -5, -5, -10, -10, -20)
    )


    private val KING_SQUARE_TABLE = arrayOf(
            arrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
            arrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
            arrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
            arrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
            arrayOf(-20, -30, -30, -40, -40, -30, -30, -20),
            arrayOf(-10, -20, -20, -20, -20, -20, -20, -10),
            arrayOf(20, 20, 0, 0, 0, 0, 20, 20),
            arrayOf(20, 30, 10, 0, 0, 10, 30, 20)
    )


    override fun evaluate(board: Array<Array<Piece?>>, color: Color): Int {
        var score = 0
        var opponentScore = 0

        for (y in 0 until 8) {
            for (x in 0 until 8) {
                if (board[y][x] != null) {
                    if (board[y][x]?.color == color)
                        score += getPieceValue(board[y][x]!!, color, x, y)
                    else
                        opponentScore += getPieceValue(board[y][x]!!, color.opposite(), x, y)
                }
            }
        }

        return score - opponentScore
    }


    private fun getPieceValue(piece: Piece, color: Color, x: Int, y: Int): Int {
        when (piece) {
            is Pawn -> {
                return 100 + if (color == Color.WHITE) PAWN_SQUARE_TABLE[y][x] else PAWN_SQUARE_TABLE[7 - y][x]
            }
            is Knight -> {
                return 320 + if (color == Color.WHITE) KNIGHT_SQUARE_TABLE[y][x] else KNIGHT_SQUARE_TABLE[7 - y][x]
            }
            is Bishop -> {
                return 330 + if (color == Color.WHITE) BISHOP_SQUARE_TABLE[y][x] else BISHOP_SQUARE_TABLE[7 - y][x]
            }
            is Rook -> {
                return 500 + if (color == Color.WHITE) ROOK_SQUARE_TABLE[y][x] else ROOK_SQUARE_TABLE[7 - y][x]
            }
            is Queen -> {
                return 900 + if (color == Color.WHITE) QUEEN_SQUARE_TABLE[y][x] else QUEEN_SQUARE_TABLE[7 - y][x]
            }
            is King -> {
                return 20000 + if (color == Color.WHITE) KING_SQUARE_TABLE[y][x] else KING_SQUARE_TABLE[7 - y][x]
            }
        }
        return 0
    }
}