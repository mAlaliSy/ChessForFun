package com.malalisy.chessforfun.pojos.pieces

import com.malalisy.chessforfun.pojos.PlayerColor


class Pawn(playerColor: PlayerColor) : Piece(playerColor) {

    override fun toString(): String {
        return "P" + if (playerColor == PlayerColor.WHITE) {
            "W"
        } else {
            "B"
        }
    }


    fun isFirstMove(y: Int): Boolean {
        return if (playerColor == PlayerColor.WHITE) y == 1 else y == 6
    }

}