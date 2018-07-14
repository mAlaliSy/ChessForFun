package com.malalisy.chessforfun.utils

import com.malalisy.chessforfun.Color
import com.malalisy.chessforfun.pieces.*


fun boardFromMap(map: String): Array<Array<Piece?>> {
    val board = Array<Array<Piece?>>(8, {
        Array<Piece?>(8, {
            null
        })
    })
    val mapList = map.replace("\n", "").split("-")
    for (i in 0..7) {
        for (j in 0..7) {

            board[i][j] = when (mapList[i * 8 + j]) {
                "." -> null
                "PW" -> Pawn(Color.WHITE)
                "PB" -> Pawn(Color.BLACK)

                "RWF" -> Rook(Color.WHITE, true) // F = Firs Move
                "RWN" -> Rook(Color.WHITE, false)
                "RBF" -> Rook(Color.BLACK, true)
                "RBN" -> Rook(Color.BLACK, false)

                "NW" -> Knight(Color.WHITE)
                "NB" -> Knight(Color.BLACK)

                "BW" -> Bishop(Color.WHITE)
                "BB" -> Bishop(Color.BLACK)

                "QW" -> Queen(Color.WHITE)
                "QB" -> Queen(Color.BLACK)

                "KWF" -> King(Color.WHITE, true)
                "KWN" -> King(Color.WHITE, false)
                "KBF" -> King(Color.BLACK, true)
                "KBN" -> King(Color.BLACK, false)

                else -> null
            }
        }
    }

    return board
}

fun getInitialMap(): String {
    return "RWF-NW-BW-QW-KWF-BW-NW-RWF-" +
            "PW-PW-PW-PW-PW-PW-PW-PW-" +
            ".-.-.-.-.-.-.-.-" +
            ".-.-.-.-.-.-.-.-" +
            ".-.-.-.-.-.-.-.-" +
            ".-.-.-.-.-.-.-.-" +
            "PB-PB-PB-PB-PB-PB-PB-PB-" +
            "RBF-NB-BB-QB-KBF-BB-NB-RBF-"
}

fun getInitialBoard(): Array<Array<Piece?>> = boardFromMap(getInitialMap())

fun boardToMap(board: Array<Array<Piece?>>): String {
    var map = ""
    for (i in 0..7) {
        for (j in 0..7) {

            var tile = ""
            if (board[i][j]?.color == Color.WHITE)
                tile = "W"
            else if (board[i][j]?.color == Color.BLACK)
                tile = "B"

            when (board[i][j]) {
                is Pawn -> tile = "P$tile"

                is Knight -> tile = "N$tile"

                is Rook -> {
                    tile += if ((board[i][j] as Rook).isFirstMove)
                        "F"
                    else "N"
                }
                is Bishop -> tile = "B$tile"
                is Queen -> tile = "Q$tile"
                is King -> {
                    tile = "K$tile"

                    tile += if ((board[i][j] as King).isFirstMove)
                        "F"
                    else "N"
                }
                else -> tile = "."
            }
            map += "$tile-"
        }
    }

    return map

}


fun printBoard(board: Array<Array<Piece?>>) {
    for (i in 0..7) {
        for (j in 0..7) {
            if (board[i][j] != null)
                print(board[i][j])
            else
                print(".")
            print("\t")
        }

        println()
    }
}