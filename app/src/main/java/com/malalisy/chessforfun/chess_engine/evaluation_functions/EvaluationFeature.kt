package com.malalisy.chessforfun.chess_engine.evaluation_functions

import com.malalisy.chessforfun.Color
import com.malalisy.chessforfun.pieces.Piece

interface EvaluationFeature {
    fun evaluate(board: Array<Array<Piece?>>, color: Color): Int
}