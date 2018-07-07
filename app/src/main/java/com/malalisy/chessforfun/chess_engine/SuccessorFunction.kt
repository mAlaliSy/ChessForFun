package com.malalisy.chessforfun.chess_engine

import com.malalisy.chessforfun.Color
import com.malalisy.chessforfun.Move
import com.malalisy.chessforfun.pieces.*
import com.malalisy.chessforfun.validPosition
import java.util.ArrayList

class SuccessorFunction() {

    fun getAllAvailableMoves(board: Array<Array<Piece?>>, color: Color, lastMove: Move?): List<Move> {
        val list = ArrayList<Move>()
        for (x in 0..7) {
            for (y in 0..7) {
                if (board[y][x] != null || board[y][x]?.color != color)
                    continue

                list.addAll(when (board[y][x]) {
                    is Pawn -> availableMovesForPawn(board, x, y, lastMove)
                    is Bishop -> availableMovesForBishop(board, x, y)
                    is Knight -> availableMovesForKnight(board, x, y)
                    is Rook -> availableMovesForRook(board, x, y)
                    is Queen -> availableMovesForQueen(board, x, y)
                    else -> availableMovesForKing(board, x, y)

                })

            }
        }
        return list
    }

    private fun availableMovesForKing(board: Array<Array<Piece?>>, x: Int, y: Int): List<Move> {
        val list = ArrayList<Move>()
        val piece = board[y][x]!!
        val xDirArray = arrayOf(1, 1, 1, 0, 0, -1, -1, -1)
        val yDirArray = arrayOf(-1, 0, 1, -1, 1, -1, 0, 1)
        for (step in 0 until xDirArray.size) {
            val x2 = x + xDirArray[step]
            val y2 = y + yDirArray[step]
            if (validPosition(x2, y2) && (board[y2][x2] == null || board[y2][x2]?.color == piece.color))
                list.add(Move(x, y, x2, y2, piece))
        }

        // Check If castling available
        if ((piece as King).isFirstMove) {
            if (piece.color == Color.WHITE) {
                // Right castling
                if (board[0][5] == null && board[0][6] == null && board[0][7] != null && board[0][7] is Rook && (board[0][7] as Rook).isFirstMove) {
                    list.add(Move(x, y, 6, 0, piece))
                }
                // Left Castling
                if (board[0][1] == null && board[0][2] == null && board[0][3] == null && board[0][0] != null && board[0][0] is Rook && (board[0][0] as Rook).isFirstMove) {
                    list.add(Move(x, y, 2, 0, piece))
                }
            } else {
                // Right Castling
                if (board[7][1] == null && board[7][2] == null && board[7][3] == null && board[7][0] != null && board[7][0] is Rook && (board[7][0] as Rook).isFirstMove) {
                    list.add(Move(x, y, 2, 7, piece))
                }
                // Left Castling
                if (board[7][5] == null && board[7][6] == null && board[7][7] != null && board[7][7] is Rook && (board[7][7] as Rook).isFirstMove) {
                    list.add(Move(x, y, 6, 7, piece))
                }
            }
        }
        return list
    }

    private fun availableMovesForQueen(board: Array<Array<Piece?>>, x: Int, y: Int): List<Move> {
        val list = ArrayList<Move>()

        val xDirArray = arrayOf(1, 1, 1, 0, 0, -1, -1, -1)
        val yDirArray = arrayOf(-1, 0, 1, -1, 1, -1, 0, 1)
        for (index in 0 until xDirArray.size) {
            list.addAll(availableMoves(board, x, y, xDirArray[index], yDirArray[index]))
        }
        return list
    }

    private fun availableMovesForRook(board: Array<Array<Piece?>>, x: Int, y: Int): List<Move> {
        val list = ArrayList<Move>()

        val xDirArray = arrayOf(1, -1, 0, 0)
        val yDirArray = arrayOf(0, 0, 1, -1)
        for (index in 0 until xDirArray.size) {
            list.addAll(availableMoves(board, x, y, xDirArray[index], yDirArray[index]))
        }
        return list
    }

    private fun availableMovesForBishop(board: Array<Array<Piece?>>, x: Int, y: Int): List<Move> {
        val list = ArrayList<Move>()

        val xDirArray = arrayOf(1, 1, -1, -1)
        val yDirArray = arrayOf(1, -1, 1, -1)
        for (index in 0 until xDirArray.size) {
            list.addAll(availableMoves(board, x, y, xDirArray[index], yDirArray[index]))
        }
        return list
    }

    private fun availableMovesForKnight(board: Array<Array<Piece?>>, x: Int, y: Int): List<Move> {
        val list = ArrayList<Move>()
        val piece = board[y][x]!!

        val xKDirArray = intArrayOf(-2, -2, 2, 2, -1, 1, -1, 1)
        val yKDirArray = intArrayOf(-1, 1, -1, 1, -2, -2, 2, 2)

        for (i in 0 until xKDirArray.size) {
            val x2 = x + xKDirArray[i]
            val y2 = y + yKDirArray[i]
            if (validPosition(x2, y2) && (board[y2][x2] == null || board[y2][x2]?.color != piece.color))
                list.add(Move(x, y, x2, y2, piece))
        }

        return list
    }

    private fun availableMovesForPawn(board: Array<Array<Piece?>>, x: Int, y: Int, lastMove: Move?): List<Move> {
        val list = ArrayList<Move>()

        val piece = board[y][x]!!

        if (piece.color == Color.WHITE) {
            // Forward Moves
            if (board[y + 1][x] == null)
                list.add(Move(x, y, x, y + 1, piece))
            if ((piece as Pawn).isFirstMove && board[y + 2][x] == null)
                list.add(Move(x, y, x, y + 2, piece))

            // Capturing moves
            if (board[y + 1][x - 1] != null && board[y + 1][x - 1]?.color != piece.color)
                list.add(Move(x, y, x - 1, y + 1, piece))
            if (board[y + 1][x + 1] != null && board[y + 1][x + 1]?.color != piece.color)
                list.add(Move(x, y, x + 1, y + 1, piece))

            // En passant
            if (lastMove != null && lastMove.piece is Pawn
                    && lastMove.y2 == lastMove.y1 - 2 && y == 4 // Checking if the pawn has the right to En passant
            ) {
                if (x == lastMove.x2 - 1)
                    list.add(Move(x, y, x + 1, y + 1, piece))
                if (x == lastMove.x2 + 1)
                    list.add(Move(x, y, x - 1, y + 1, piece))
            }


        } else {
            // Forward Moves
            if (board[y - 1][x] == null)
                list.add(Move(x, y, x, y - 1, piece))
            if ((piece as Pawn).isFirstMove && board[y - 2][x] == null)
                list.add(Move(x, y, x, y - 2, piece))

            // Capturing moves
            if (board[y - 1][x - 1] != null && board[y - 1][x - 1]?.color != piece.color)
                list.add(Move(x, y, x - 1, y - 1, piece))
            if (board[y - 1][x + 1] != null && board[y - 1][x + 1]?.color != piece.color)
                list.add(Move(x, y, x + 1, y - 1, piece))

            // En passant
            if (lastMove != null && lastMove.piece is Pawn
                    && lastMove.y2 == lastMove.y1 + 2 && y == 3 // Checking if the pawn has the right to En passant
            ) {
                if (x == lastMove.x2 - 1)
                    list.add(Move(x, y, x + 1, y - 1, piece))
                if (x == lastMove.x2 + 1)
                    list.add(Move(x, y, x - 1, y - 1, piece))
            }
        }

        return list
    }

    private fun availableMoves(board: Array<Array<Piece?>>, x: Int, y: Int, incX: Int, incY: Int): List<Move> {
        val list = ArrayList<Move>()
        val piece = board[y][x]!!
        var x2 = x
        var y2 = y
        do {
            x2 += incX
            y2 += incY

            if (!validPosition(x, y))
                break

            if (board[y2][x2] != null) {
                if (board[y2][x2]?.color != board[y][x]?.color)
                    list.add(Move(x, y, x2, y2, piece))
                break
            }
            list.add(Move(x, y, x2, y2, piece))
        } while (true)
        return list
    }


}