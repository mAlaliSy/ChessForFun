package com.malalisy.chessforfun.pojos.pieces

import com.malalisy.chessforfun.pojos.PlayerColor

class Queen(playerColor: PlayerColor) : Piece(playerColor) {


    override fun toString(): String {
        return "Q" + if (playerColor == PlayerColor.WHITE) "W" else "B"
    }

}
