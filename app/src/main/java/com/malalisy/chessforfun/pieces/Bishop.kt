package com.malalisy.chessforfun.pieces

import com.malalisy.chessforfun.Color

class Bishop(color: Color) : Piece(color) {

    override fun toString(): String {
        return "B" + if (color == Color.WHITE) "W" else "B"
    }
}
