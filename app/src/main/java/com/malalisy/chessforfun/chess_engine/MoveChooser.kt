package com.malalisy.chessforfun.chess_engine

import com.malalisy.chessforfun.Color
import com.malalisy.chessforfun.Move
import com.malalisy.chessforfun.pieces.Piece
import com.malalisy.chessforfun.utils.copyBoard
import com.malalisy.chessforfun.utils.movePiece
import kotlin.math.max
import kotlin.math.min

class MoveChooser {

    fun chooseMove(board: Array<Array<Piece?>>, color: Color, difficulty: Int, lastMove: Move): Move = alphaBetaSearch(board, color, difficulty, lastMove)

    private fun alphaBetaSearch(board: Array<Array<Piece?>>, color: Color, difficulty: Int, lastMove: Move): Move {

        var bestMove: Move? = null
        var maxUtility = Double.NEGATIVE_INFINITY

        for (move in SuccessorFunction().getAllAvailableMoves(board, color, lastMove)) {
            val cBoard = copyBoard(board)
            movePiece(cBoard, move) // New state after making the action
            val moveUil = minValue(cBoard, color, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0, difficulty, lastMove)

            if (moveUil > maxUtility) {
                bestMove = move
                maxUtility = moveUil
            }

        }

        return bestMove!!
    }

    private fun maxValue(board: Array<Array<Piece?>>, color: Color, alpha: Double, beta: Double, depth: Int, difficulty: Int, lastMove: Move): Double {
        if (teminalTest(board, depth, difficulty)) {
            /*TODO return the evaluated value of the board*/
            return 0.0
        }
        var v = Double.NEGATIVE_INFINITY
        var a = alpha

        for (move in SuccessorFunction().getAllAvailableMoves(board, color, lastMove)) {
            val cBoard = copyBoard(board)
            movePiece(cBoard, move) // New state after making the action
            v = max(v, minValue(cBoard, color, a, beta, depth + 1, difficulty, move))
            a = max(a, v)
            if (a >= beta)
                break
        }
        return v
    }

    private fun minValue(board: Array<Array<Piece?>>, color: Color, alpha: Double, beta: Double, depth: Int, difficulty: Int, lastMove: Move): Double {
        if (teminalTest(board, depth, difficulty)) {
            /*TODO return the evaluated value of the board*/
            return 0.0
        }
        var v = Double.POSITIVE_INFINITY
        var b = beta

        for (move in SuccessorFunction().getAllAvailableMoves(board, color, lastMove)) {
            val cBoard = copyBoard(board)
            movePiece(cBoard, move) // New state after making the action
            v = min(v, maxValue(cBoard, color, alpha, beta, depth + 1, difficulty, move))
            b = min(b, v)
            if (alpha >= b)
                break
        }
        return v
    }


    private fun teminalTest(board: Array<Array<Piece?>>, depth: Int, difficulty: Int): Boolean {
        /*TODO Check if it is a checkmate*/
        return depth == difficulty
    }


}