package com.malalisy.chessforfun.pojos.pieces

import com.malalisy.chessforfun.pojos.PlayerColor

class Knight(playerColor: PlayerColor) : Piece(playerColor) {


    override fun toString(): String {
        return "N" + if (playerColor == PlayerColor.WHITE) "W" else "B"
    }

}
