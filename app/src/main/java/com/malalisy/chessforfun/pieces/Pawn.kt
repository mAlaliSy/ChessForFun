package com.malalisy.chessforfun.pieces

import com.malalisy.chessforfun.Color


class Pawn(color: Color) : Piece(color) {

    override fun toString(): String {
        return "P" + if (color == Color.WHITE) {
            "W"
        } else {
            "B"
        }
    }


    fun isFirstMove(y: Int): Boolean {
        return if (color == Color.WHITE) y == 1 else y == 6
    }

}