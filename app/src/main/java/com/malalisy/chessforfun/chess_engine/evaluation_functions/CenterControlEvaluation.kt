package com.malalisy.chessforfun.chess_engine.evaluation_functions

import com.malalisy.chessforfun.Color
import com.malalisy.chessforfun.Point
import com.malalisy.chessforfun.pieces.Piece
import com.malalisy.chessforfun.utils.getPieceCanAttack

class CenterControlEvaluation : EvaluationFeature {
    override fun evaluate(board: Array<Array<Piece?>>, color: Color): Int {

        val centerPoints = arrayOf(Point(3, 3), Point(3, 4), Point(4, 3), Point(4, 4))

        var score = 0
        var opponentScore = 0

        for (point in centerPoints) {
            score += getPieceCanAttack(board, point, color, null).size * 50
            opponentScore += getPieceCanAttack(board, point, color.opposite(), null).size * 50
        }

        return score - opponentScore
    }

}