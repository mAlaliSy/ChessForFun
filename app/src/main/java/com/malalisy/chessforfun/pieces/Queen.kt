package com.malalisy.chessforfun.pieces

import com.malalisy.chessforfun.Color

class Queen(color: Color) : Piece(color) {


    override fun toString(): String {
        return "Q" + if (color == Color.WHITE) "W" else "B"
    }

}
