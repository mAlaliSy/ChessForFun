package com.malalisy.chessforfun.chess_engine.evaluation_functions

import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.Point
import com.malalisy.chessforfun.pojos.pieces.Piece
import com.malalisy.chessforfun.utils.getPieceCanAttack

class CenterControlEvaluation : EvaluationFeature {
    override fun evaluate(board: Array<Array<Piece?>>, playerColor: PlayerColor): Int {

        val centerPoints = arrayOf(Point(3, 3), Point(3, 4), Point(4, 3), Point(4, 4))

        var score = 0
        var opponentScore = 0

        for (point in centerPoints) {
            score += getPieceCanAttack(board, point, playerColor, null).size * 50
            opponentScore += getPieceCanAttack(board, point, playerColor.opposite(), null).size * 50
        }

        return score - opponentScore
    }

}