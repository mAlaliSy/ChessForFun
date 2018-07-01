package com.malalisy.chessforfun

enum class PieceType {
    PAWN {
        override fun toString() = ""
    },
    KNIGHT {
        override fun toString() = "N"
    },
    BISHOP {
        override fun toString() = "B"

    },
    ROOK {
        override fun toString() = "R"
    },
    QUEEN {
        override fun toString() = "Q"
    },
    KING {
        override fun toString() = "K"
    }
}