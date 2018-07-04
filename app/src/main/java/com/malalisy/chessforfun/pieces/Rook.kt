package com.malalisy.chessforfun.pieces

import com.malalisy.chessforfun.Color

class Rook(color: Color, var isFirstMove: Boolean) : Piece(color) {
    override fun toString(): String {
        return "R" + if (color == Color.WHITE) {
            "W"
        } else {
            "B"
        } + if (isFirstMove) "F" else "N"
    }
}
