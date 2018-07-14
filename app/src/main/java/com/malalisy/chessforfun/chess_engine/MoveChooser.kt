package com.malalisy.chessforfun.chess_engine

import com.malalisy.chessforfun.Color
import com.malalisy.chessforfun.Move
import com.malalisy.chessforfun.pieces.Piece
import com.malalisy.chessforfun.utils.copyBoard
import com.malalisy.chessforfun.utils.isCheckMate
import com.malalisy.chessforfun.utils.movePiece
import kotlin.math.max
import kotlin.math.min

class MoveChooser(val mColor: Color) {

    val evaluationFunction = EvaluationFunction()

    /*
    * Alpha-Beta Search
    * */
    fun chooseMove(board: Array<Array<Piece?>>, difficulty: Int, lastMove: Move?): Move {
        var bestMove: Move? = null
        var maxUtility = Double.NEGATIVE_INFINITY

        for (move in SuccessorFunction().getAllAvailableMoves(board, mColor, lastMove)) {
            val cBoard = copyBoard(board)
            movePiece(cBoard, move) // New state after making the action
            val moveUil = minValue(cBoard, mColor.opposite(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, difficulty - 1, move)

            if (moveUil > maxUtility) {
                bestMove = move
                maxUtility = moveUil
            }

        }

        return bestMove!!
    }

    private fun maxValue(board: Array<Array<Piece?>>, color: Color, alpha: Double, beta: Double, depth: Int, lastMove: Move?): Double {
        if (teminalTest(board, depth, mColor, lastMove)) {
            return evaluationFunction.evaluate(board, mColor, lastMove).toDouble()
        }
        var v = Double.NEGATIVE_INFINITY
        var a = alpha

        for (move in SuccessorFunction().getAllAvailableMoves(board, color, lastMove)) {
            val cBoard = copyBoard(board)
            movePiece(cBoard, move) // New state after making the action
            v = max(v, minValue(cBoard, color.opposite(), a, beta, depth - 1, move))
            a = max(a, v)
            if (a >= beta)
                break
        }
        return v
    }

    private fun minValue(board: Array<Array<Piece?>>, color: Color, alpha: Double, beta: Double, depth: Int, lastMove: Move?): Double {
        if (teminalTest(board, depth, mColor, lastMove)) {
            return evaluationFunction.evaluate(board, mColor, lastMove).toDouble()
        }
        var v = Double.POSITIVE_INFINITY
        var b = beta

        for (move in SuccessorFunction().getAllAvailableMoves(board, color, lastMove)) {
            val cBoard = copyBoard(board)
            movePiece(cBoard, move) // New state after making the action
            v = min(v, maxValue(cBoard, color.opposite(), alpha, beta, depth - 1, move))
            b = min(b, v)
            if (alpha >= b)
                break
        }
        return v
    }


    private fun teminalTest(board: Array<Array<Piece?>>, depth: Int, color: Color, lastMove: Move?): Boolean {
        /*
        * TODO: CHECK FOR DRAW (NO VALID MOVES OR INSUFFICIENT MATERIALS)
        * */
        return depth == 0 || isCheckMate(board, color, lastMove) || isCheckMate(board, color.opposite(), lastMove)
    }
}