package com.malalisy.chessforfun.chess_engine.evaluation_functions

import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.pieces.Pawn
import com.malalisy.chessforfun.pojos.pieces.Piece

class DoubledPawnsEvaluation : EvaluationFeature {
    override fun evaluate(board: Array<Array<Piece?>>, playerColor: PlayerColor): Int {
        var score = 0
        var opponentScore = 0

        /*
        * Go through each file
        * */
        for (x in 0 until 8) {

            var nPawns = 0
            var opponentNPawns = 0

            /*
            * Find the number of pawns in thi file
            * */
            for (y in 1 until 8) {
                if (board[y][x] != null && board[y][x] is Pawn) {
                    if (board[y][x]?.playerColor == playerColor) {
                        nPawns++
                    } else {
                        opponentNPawns++
                    }
                }
            }

            /*
            * Check if there is doubled pawns in this file
            * */
            if (nPawns > 1) {
                score += nPawns * -25
            }
            if (opponentNPawns > 1)
                opponentScore += opponentNPawns * -25

        }

        return score - opponentScore
    }
}