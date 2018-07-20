package com.malalisy.chessforfun.chess_engine

import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.pieces.Piece
import com.malalisy.chessforfun.utils.copyBoard
import com.malalisy.chessforfun.utils.isCheckMate
import com.malalisy.chessforfun.utils.isDrawByStalemate
import com.malalisy.chessforfun.utils.movePiece
import kotlin.math.max
import kotlin.math.min

class MoveChooser(val mPlayerColor: PlayerColor) {

    val evaluationFunction = EvaluationFunction()

    /*
    * Alpha-Beta Search
    * */
    fun chooseMove(board: Array<Array<Piece?>>, difficulty: Int, lastMove: com.malalisy.chessforfun.pojos.Move?): com.malalisy.chessforfun.pojos.Move {
        var bestMove: com.malalisy.chessforfun.pojos.Move? = null
        var maxUtility = Double.NEGATIVE_INFINITY

        for (move in MovesGenerator.getAllAvailableMoves(board, mPlayerColor, lastMove)) {
            val cBoard = copyBoard(board)
            movePiece(cBoard, move) // New state after making the action
            val moveUil = minValue(cBoard, mPlayerColor.opposite(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, difficulty - 1, move)

            if (moveUil > maxUtility) {
                bestMove = move
                maxUtility = moveUil
            }

        }


        return bestMove!!
    }

    private fun maxValue(board: Array<Array<Piece?>>, playerColor: PlayerColor, alpha: Double, beta: Double, depth: Int, lastMove: com.malalisy.chessforfun.pojos.Move?): Double {
        if (teminalTest(board, depth, mPlayerColor, lastMove)) {
            return evaluationFunction.evaluate(board, mPlayerColor, lastMove).toDouble()
        }
        var v = Double.NEGATIVE_INFINITY
        var a = alpha

        for (move in MovesGenerator.getAllAvailableMoves(board, playerColor, lastMove)) {
            val cBoard = copyBoard(board)
            movePiece(cBoard, move) // New state after making the action
            v = max(v, minValue(cBoard, playerColor.opposite(), a, beta, depth - 1, move))
            a = max(a, v)
            if (a >= beta)
                break
        }
        return v
    }

    private fun minValue(board: Array<Array<Piece?>>, playerColor: PlayerColor, alpha: Double, beta: Double, depth: Int, lastMove: com.malalisy.chessforfun.pojos.Move?): Double {
        if (teminalTest(board, depth, mPlayerColor, lastMove)) {
            return evaluationFunction.evaluate(board, mPlayerColor, lastMove).toDouble()
        }
        var v = Double.POSITIVE_INFINITY
        var b = beta

        for (move in MovesGenerator.getAllAvailableMoves(board, playerColor, lastMove)) {
            val cBoard = copyBoard(board)
            movePiece(cBoard, move) // New state after making the action
            v = min(v, maxValue(cBoard, playerColor.opposite(), alpha, beta, depth - 1, move))
            b = min(b, v)
            if (alpha >= b)
                break
        }
        return v
    }


    private fun teminalTest(board: Array<Array<Piece?>>, depth: Int, playerColor: PlayerColor, lastMove: com.malalisy.chessforfun.pojos.Move?): Boolean {
        /*
        * TODO: CHECK FOR DRAW (INSUFFICIENT MATERIALS)
        * */
        return depth == 0 || isCheckMate(board, playerColor, lastMove) || isCheckMate(board, playerColor.opposite(), lastMove) || isDrawByStalemate(board, playerColor, lastMove)
    }
}