package com.malalisy.chessforfun.chess_engine

import com.malalisy.chessforfun.Color
import com.malalisy.chessforfun.Move
import com.malalisy.chessforfun.Point
import com.malalisy.chessforfun.pieces.*
import com.malalisy.chessforfun.utils.*
import java.util.ArrayList

class SuccessorFunction() {

    fun getAllAvailableMoves(board: Array<Array<Piece?>>, color: Color, lastMove: Move?): List<Move> {
        val list = ArrayList<Move>()
        for (x in 0..7) {
            for (y in 0..7) {
                if (board[y][x] == null || board[y][x]?.color != color)
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

    private fun availableMovesForKing(board: Array<Array<Piece?>>, point: Point): List<Move> {
        val list = ArrayList<Move>()
        val piece = board[point.y][point.x]!!


        for (step in 0 until kingDirArray.size) {
            val to = point + kingDirArray[step]
            if (validPosition(to.x, to.y) && (board[to.y][to.y] == null || board[to.y][to.x]?.color != piece.color))
                list.add(Move(point, to, piece))
        }

        // Check If castling available
        if ((piece as King).isFirstMove) {
            if (piece.color == Color.WHITE) {
                // Right castling
                var castling = Move(point, Point(6, 0), piece)
                if (isValidCastlingMove(board, castling))
                    list.add(castling)

                // Left Castling
                castling = Move(point, Point(2, 0), piece)
                if (isValidCastlingMove(board, castling))
                    list.add(castling)


            } else {

                // Right castling
                var castling = Move(point, Point(2, 7), piece)
                if (isValidCastlingMove(board, castling))
                    list.add(castling)

                // Left Castling
                castling = Move(point, Point(6, 7), piece)
                if (isValidCastlingMove(board, castling))
                    list.add(castling)
            }
        }
        return list
    }

    private fun availableMovesForQueen(board: Array<Array<Piece?>>, point: Point): List<Move> {
        val list = ArrayList<Move>()

        for (d in horiVertDirArray) {
            list.addAll(availableMoves(board, point, d))
        }
        for (d in diagonalDirArray) {
            list.addAll(availableMoves(board, point, d))
        }

        return list
    }

    private fun availableMovesForRook(board: Array<Array<Piece?>>, point: Point): List<Move> {
        val list = ArrayList<Move>()


        for (d in horiVertDirArray) {
            list.addAll(availableMoves(board, point, d))
        }
        return list
    }

    private fun availableMovesForBishop(board: Array<Array<Piece?>>, point: Point): List<Move> {
        val list = ArrayList<Move>()

        for (d in diagonalDirArray) {
            list.addAll(availableMoves(board, point, d))
        }
        return list
    }

    private fun availableMovesForKnight(board: Array<Array<Piece?>>, point: Point): List<Move> {
        val list = ArrayList<Move>()
        val piece = board[point.y][point.x]!!

        for (i in 0 until knightDirArray.size) {
            val to = point + knightDirArray[i]
            if (validPosition(to.x, to.y) && (board[to.y][to.x] == null || board[to.y][to.x]?.color != piece.color))
                list.add(Move(point, to, piece))
        }

        return list
    }

    private fun availableMovesForPawn(board: Array<Array<Piece?>>, point: Point, lastMove: Move?): List<Move> {
        val list = ArrayList<Move>()

        val piece = board[point.y][point.x]!!

        if (piece.color == Color.WHITE) {
            // Forward Moves
            if (validPosition(point.x, point.y + 1) && board[point.y + 1][point.x] == null)
                list.add(Move(point, point addToY 1, piece))
            if ((piece as Pawn).isFirstMove(point.y) && board[point.y + 2][point.x] == null)
                list.add(Move(point, point addToY 2, piece))

            // Capturing moves
            if (validPosition(point.x - 1, point.y + 1) && board[point.y + 1][point.x - 1] != null && board[point.y + 1][point.x - 1]?.color != piece.color)
                list.add(Move(Point(point.x, point.y), Point(point.x - 1, point.y + 1), piece))
            if (validPosition(point.x + 1, point.y + 1) && board[point.y + 1][point.x + 1] != null && board[point.y + 1][point.x + 1]?.color != piece.color)
                list.add(Move(point, point addToY 1 addToX 1, piece))

            // En passant
            if (lastMove != null && lastMove.piece is Pawn
                    && lastMove.to.y == lastMove.from.y - 2 && point.y == 4 // Checking if the pawn has the right to En passant
            ) {
                if (point.x == lastMove.to.x - 1)
                    list.add(Move(point, point addToX 1 addToY 1, piece))
                if (point.x == lastMove.to.x + 1)
                    list.add(Move(point, point addToX -1 addToY 1, piece))
            }


        } else {
            // Forward Moves
            if (validPosition(point.x, point.y - 1) && board[point.y - 1][point.x] == null)
                list.add(Move(point, point addToY -1, piece))
            if ((piece as Pawn).isFirstMove(point.y) && board[point.y - 2][point.x] == null)
                list.add(Move(point, point addToY -2, piece))
            // Capturing moves
            if (validPosition(point.x - 1, point.y - 1) && board[point.y - 1][point.x - 1] != null && board[point.y - 1][point.x - 1]?.color != piece.color) {
                list.add(Move(point, point addToY -1 addToX -1, piece))
            }
            if (validPosition(point.x + 1, point.y - 1) && board[point.y - 1][point.x + 1] != null && board[point.y - 1][point.x + 1]?.color != piece.color)
                list.add(Move(point, point addToX 1 addToY -1, piece))

            // En passant
            if (lastMove != null && lastMove.piece is Pawn
                    && lastMove.to.y == lastMove.from.y + 2 && point.y == 3 // Checking if the pawn has the right to En passant
            ) {
                if (point.x == lastMove.to.x - 1)
                    list.add(Move(point, point addToX 1 addToY -1, piece))
                if (point.x == lastMove.to.x + 1)
                    list.add(Move(point, point addToY -1 addToX -1, piece))
            }
        }

        return list
    }

    private fun availableMoves(board: Array<Array<Piece?>>, point: Point, inc: Point): List<Move> {
        val list = ArrayList<Move>()
        val piece = board[point.y][point.x]!!
        var to = point
        do {
            to += inc
            if (!validPosition(to.x, to.y)) {
                break
            }

            if (board[to.y][to.x] != null) {
                if (piece.color != board[to.y][to.x]?.color)
                    list.add(Move(point, to, piece))
                break
            }
            list.add(Move(point, to, piece))
        } while (true)
        return list
    }


}