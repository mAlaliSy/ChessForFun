package com.malalisy.chessforfun.pieces

import com.malalisy.chessforfun.Color


class Pawn(color: Color, var isFirstMove: Boolean) : Piece(color) {

    override fun toString(): String {
        return "P" + if (color == Color.WHITE) {
            "W"
        } else {
            "B"
        }
    }


}