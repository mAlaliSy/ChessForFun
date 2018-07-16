package com.malalisy.chessforfun.pojos

enum class PlayerColor {
    WHITE, BLACK;


    fun opposite(): PlayerColor {
        if (this == BLACK)
            return WHITE
        else
            return BLACK
    }
}
