package com.malalisy.chessforfun.pojos.pieces

import com.malalisy.chessforfun.pojos.PlayerColor

class Bishop(playerColor: PlayerColor) : Piece(playerColor) {

    override fun toString(): String {
        return "B" + if (playerColor == PlayerColor.WHITE) "W" else "B"
    }
}
