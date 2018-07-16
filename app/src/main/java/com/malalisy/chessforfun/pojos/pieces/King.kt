package com.malalisy.chessforfun.pojos.pieces

import com.malalisy.chessforfun.pojos.PlayerColor


class King(playerColor: PlayerColor, var isFirstMove: Boolean) : Piece(playerColor) {

    override fun toString(): String {
        return "K" + if (playerColor == PlayerColor.WHITE) {
            "W"
        } else {
            "B"
        } + if (isFirstMove) "F" else "N"
    }

}