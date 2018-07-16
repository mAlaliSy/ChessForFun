package com.malalisy.chessforfun.chess_engine

import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.Move
import com.malalisy.chessforfun.pojos.Point
import com.malalisy.chessforfun.pojos.pieces.*
import com.malalisy.chessforfun.utils.*
import java.util.ArrayList

class SuccessorFunction() {

    fun getAllAvailableMoves(board: Array<Array<Piece?>>, playerColor: PlayerColor, lastMove: com.malalisy.chessforfun.pojos.Move?): List<com.malalisy.chessforfun.pojos.Move> {
        val list = ArrayList<com.malalisy.chessforfun.pojos.Move>()
        for (x in 0..7) {
            for (y in 0..7) {
                if (board[y][x] == null || board[y][x]?.playerColor != playerColor)
                    continue
                list.addAll(when (board[y][x]) {
                    is Pawn -> availableMovesForPawn(board, Point(x, y), lastMove)
                    is Bishop -> availableMovesForBishop(board, Point(x, y))
                    is Knight -> availableMovesForKnight(board, Point(x, y))
                    is Rook -> availableMovesForRook(board, Point(x, y))
                    is Queen -> availableMovesForQueen(board, Point(x, y))
                    else -> availableMovesForKing(board, Point(x, y))

                })

            }
        }

        return list.filter { !willResultInCheck(board, it) }
    }

    private fun availableMovesForKing(board: Array<Array<Piece?>>, point: Point): List<com.malalisy.chessforfun.pojos.Move> {
        val list = ArrayList<com.malalisy.chessforfun.pojos.Move>()
        val piece = board[point.y][point.x]!!


        for (step in 0 until kingDirArray.size) {
            val to = point + kingDirArray[step]
            if (validPosition(to.x, to.y) && (board[to.y][to.y] == null || board[to.y][to.x]?.playerColor != piece.playerColor))
                list.add(com.malalisy.chessforfun.pojos.Move(point, to, piece))
        }

        // Check If castling available
        if ((piece as King).isFirstMove) {
            if (piece.playerColor == PlayerColor.WHITE) {
                // Right castling
                var castling = com.malalisy.chessforfun.pojos.Move(point, Point(6, 0), piece)
                if (isValidCastlingMove(board, castling))
                    list.add(castling)

                // Left Castling
                castling = com.malalisy.chessforfun.pojos.Move(point, Point(2, 0), piece)
                if (isValidCastlingMove(board, castling))
                    list.add(castling)


            } else {

                // Right castling
                var castling = com.malalisy.chessforfun.pojos.Move(point, Point(2, 7), piece)
                if (isValidCastlingMove(board, castling))
                    list.add(castling)

                // Left Castling
                castling = com.malalisy.chessforfun.pojos.Move(point, Point(6, 7), piece)
                if (isValidCastlingMove(board, castling))
                    list.add(castling)
            }
        }
        return list
    }

    private fun availableMovesForQueen(board: Array<Array<Piece?>>, point: Point): List<com.malalisy.chessforfun.pojos.Move> {
        val list = ArrayList<com.malalisy.chessforfun.pojos.Move>()

        for (d in horiVertDirArray) {
            list.addAll(availableMoves(board, point, d))
        }
        for (d in diagonalDirArray) {
            list.addAll(availableMoves(board, point, d))
        }

        return list
    }

    private fun availableMovesForRook(board: Array<Array<Piece?>>, point: Point): List<com.malalisy.chessforfun.pojos.Move> {
        val list = ArrayList<com.malalisy.chessforfun.pojos.Move>()


        for (d in horiVertDirArray) {
            list.addAll(availableMoves(board, point, d))
        }
        return list
    }

    private fun availableMovesForBishop(board: Array<Array<Piece?>>, point: Point): List<com.malalisy.chessforfun.pojos.Move> {
        val list = ArrayList<com.malalisy.chessforfun.pojos.Move>()

        for (d in diagonalDirArray) {
            list.addAll(availableMoves(board, point, d))
        }
        return list
    }

    private fun availableMovesForKnight(board: Array<Array<Piece?>>, point: Point): List<com.malalisy.chessforfun.pojos.Move> {
        val list = ArrayList<com.malalisy.chessforfun.pojos.Move>()
        val piece = board[point.y][point.x]!!

        for (i in 0 until knightDirArray.size) {
            val to = point + knightDirArray[i]
            if (validPosition(to.x, to.y) && (board[to.y][to.x] == null || board[to.y][to.x]?.playerColor != piece.playerColor))
                list.add(com.malalisy.chessforfun.pojos.Move(point, to, piece))
        }

        return list
    }

    private fun availableMovesForPawn(board: Array<Array<Piece?>>, point: Point, lastMove: com.malalisy.chessforfun.pojos.Move?): List<com.malalisy.chessforfun.pojos.Move> {
        val list = ArrayList<com.malalisy.chessforfun.pojos.Move>()

        val piece = board[point.y][point.x]!!

        if (piece.playerColor == PlayerColor.WHITE) {
            // Forward Moves
            if (validPosition(point.x, point.y + 1) && board[point.y + 1][point.x] == null)
                list.add(com.malalisy.chessforfun.pojos.Move(point, point addToY 1, piece))
            if ((piece as Pawn).isFirstMove(point.y) && board[point.y + 2][point.x] == null)
                list.add(com.malalisy.chessforfun.pojos.Move(point, point addToY 2, piece))

            // Capturing moves
            if (validPosition(point.x - 1, point.y + 1) && board[point.y + 1][point.x - 1] != null && board[point.y + 1][point.x - 1]?.playerColor != piece.playerColor)
                list.add(com.malalisy.chessforfun.pojos.Move(Point(point.x, point.y), Point(point.x - 1, point.y + 1), piece))
            if (validPosition(point.x + 1, point.y + 1) && board[point.y + 1][point.x + 1] != null && board[point.y + 1][point.x + 1]?.playerColor != piece.playerColor)
                list.add(com.malalisy.chessforfun.pojos.Move(point, point addToY 1 addToX 1, piece))

            // En passant
            if (lastMove != null && lastMove.piece is Pawn
                    && lastMove.to.y == lastMove.from.y - 2 && point.y == 4 // Checking if the pawn has the right to En passant
            ) {
                if (point.x == lastMove.to.x - 1)
                    list.add(com.malalisy.chessforfun.pojos.Move(point, point addToX 1 addToY 1, piece))
                if (point.x == lastMove.to.x + 1)
                    list.add(com.malalisy.chessforfun.pojos.Move(point, point addToX -1 addToY 1, piece))
            }


        } else {
            // Forward Moves
            if (validPosition(point.x, point.y - 1) && board[point.y - 1][point.x] == null)
                list.add(com.malalisy.chessforfun.pojos.Move(point, point addToY -1, piece))
            if ((piece as Pawn).isFirstMove(point.y) && board[point.y - 2][point.x] == null)
                list.add(com.malalisy.chessforfun.pojos.Move(point, point addToY -2, piece))
            // Capturing moves
            if (validPosition(point.x - 1, point.y - 1) && board[point.y - 1][point.x - 1] != null && board[point.y - 1][point.x - 1]?.playerColor != piece.playerColor) {
                list.add(com.malalisy.chessforfun.pojos.Move(point, point addToY -1 addToX -1, piece))
            }
            if (validPosition(point.x + 1, point.y - 1) && board[point.y - 1][point.x + 1] != null && board[point.y - 1][point.x + 1]?.playerColor != piece.playerColor)
                list.add(com.malalisy.chessforfun.pojos.Move(point, point addToX 1 addToY -1, piece))

            // En passant
            if (lastMove != null && lastMove.piece is Pawn
                    && lastMove.to.y == lastMove.from.y + 2 && point.y == 3 // Checking if the pawn has the right to En passant
            ) {
                if (point.x == lastMove.to.x - 1)
                    list.add(com.malalisy.chessforfun.pojos.Move(point, point addToX 1 addToY -1, piece))
                if (point.x == lastMove.to.x + 1)
                    list.add(com.malalisy.chessforfun.pojos.Move(point, point addToY -1 addToX -1, piece))
            }
        }

        return list
    }

    private fun availableMoves(board: Array<Array<Piece?>>, point: Point, inc: Point): List<com.malalisy.chessforfun.pojos.Move> {
        val list = ArrayList<com.malalisy.chessforfun.pojos.Move>()
        val piece = board[point.y][point.x]!!
        var to = point
        do {
            to += inc
            if (!validPosition(to.x, to.y)) {
                break
            }

            if (board[to.y][to.x] != null) {
                if (piece.playerColor != board[to.y][to.x]?.playerColor)
                    list.add(com.malalisy.chessforfun.pojos.Move(point, to, piece))
                break
            }
            list.add(com.malalisy.chessforfun.pojos.Move(point, to, piece))
        } while (true)
        return list
    }


}