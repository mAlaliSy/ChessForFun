package com.malalisy.chessforfun.chess_engine.evaluation_functions

import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.pieces.Piece

interface EvaluationFeature {
    fun evaluate(board: Array<Array<Piece?>>, playerColor: PlayerColor): Int
}