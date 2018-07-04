package com.malalisy.chessforfun

import com.malalisy.chessforfun.pieces.Piece
import com.malalisy.chessforfun.pieces.Rook

fun validPosition(x: Int, y: Int) = x in 0..7 && y in 0..7

fun copyBoard(board: Array<Array<Piece?>>): Array<Array<Piece?>> {
    var cboard = Array<Array<Piece?>>(8, {
        Array<Piece?>(8, { index ->
            board[it][index]
        })
    })
    return cboard

}