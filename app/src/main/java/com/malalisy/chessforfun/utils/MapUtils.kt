package com.malalisy.chessforfun.utils

import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.pieces.*


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
                "PW" -> Pawn(PlayerColor.WHITE)
                "PB" -> Pawn(PlayerColor.BLACK)

                "RWF" -> Rook(PlayerColor.WHITE, true) // F = Firs Move
                "RWN" -> Rook(PlayerColor.WHITE, false)
                "RBF" -> Rook(PlayerColor.BLACK, true)
                "RBN" -> Rook(PlayerColor.BLACK, false)

                "NW" -> Knight(PlayerColor.WHITE)
                "NB" -> Knight(PlayerColor.BLACK)

                "BW" -> Bishop(PlayerColor.WHITE)
                "BB" -> Bishop(PlayerColor.BLACK)

                "QW" -> Queen(PlayerColor.WHITE)
                "QB" -> Queen(PlayerColor.BLACK)

                "KWF" -> King(PlayerColor.WHITE, true)
                "KWN" -> King(PlayerColor.WHITE, false)
                "KBF" -> King(PlayerColor.BLACK, true)
                "KBN" -> King(PlayerColor.BLACK, false)

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
            if (board[i][j]?.playerColor == PlayerColor.WHITE)
                tile = "W"
            else if (board[i][j]?.playerColor == PlayerColor.BLACK)
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