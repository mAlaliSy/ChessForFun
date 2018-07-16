package com.malalisy.chessforfun.chess_engine

import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.chess_engine.evaluation_functions.CenterControlEvaluation
import com.malalisy.chessforfun.chess_engine.evaluation_functions.DoubledPawnsEvaluation
import com.malalisy.chessforfun.chess_engine.evaluation_functions.EvaluationFeature
import com.malalisy.chessforfun.chess_engine.evaluation_functions.MaterialEvaluation
import com.malalisy.chessforfun.pojos.pieces.*
import com.malalisy.chessforfun.utils.isCheckMate

class EvaluationFunction {

    var evaluationFeatures: Array<EvaluationFeature>

    init {
        evaluationFeatures = arrayOf(
                MaterialEvaluation(),
                CenterControlEvaluation(),
                DoubledPawnsEvaluation()
        )
    }

    fun evaluate(board: Array<Array<Piece?>>, playerColor: PlayerColor, lastMove: com.malalisy.chessforfun.pojos.Move?): Int {
        if (isCheckMate(board, playerColor, lastMove))
            return Int.MIN_VALUE
        else if (isCheckMate(board, playerColor.opposite(), lastMove))
            return Int.MAX_VALUE

        var totalEvaluation = 0
        for (evaluationFeature in evaluationFeatures)
            totalEvaluation += evaluationFeature.evaluate(board, playerColor)

        return totalEvaluation
    }
}