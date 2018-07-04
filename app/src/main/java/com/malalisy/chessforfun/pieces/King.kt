package com.malalisy.chessforfun.pieces

import com.malalisy.chessforfun.Color


class King(color: Color, var isFirstMove: Boolean) : Piece(color) {

    override fun toString(): String {
        return "K" + if (color == Color.WHITE) {
            "W"
        } else {
            "B"
        } + if (isFirstMove) "F" else "N"
    }

}