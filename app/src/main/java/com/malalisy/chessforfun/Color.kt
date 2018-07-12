package com.malalisy.chessforfun

enum class Color {
    WHITE, BLACK;


    fun opposite(): Color {
        if (this == BLACK)
            return WHITE
        else
            return BLACK
    }
}
