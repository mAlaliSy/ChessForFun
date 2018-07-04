package com.malalisy.chessforfun.pieces

import com.malalisy.chessforfun.Color

class Knight(color: Color) : Piece(color) {


    override fun toString(): String {
        return "N" + if (color == Color.WHITE) "W" else "B"
    }

}
