package com.malalisy.chessforfun

import com.malalisy.chessforfun.pieces.*
import com.malalisy.chessforfun.utils.isLegalMove
import com.malalisy.chessforfun.utils.movePiece
import kotlin.math.abs

class GameController(var board: Array<Array<Piece?>>, var lastMove: Move?) {

    fun move(p1: Point, p2: Point): Move? {
        if (board[p1.y][p1.x] == null) {
            return null
        }

        val move = Move(p1, p2, board[p1.y][p1.x]!!)

        if (isLegalMove(board, move, lastMove)) {
            return null
        }

        when (board[p1.y][p1.x]) {

            is Pawn -> {
                processPawnMove(move)
            }
            is King -> {
                processKingMove(move)
            }
            else -> {
                processNormalMove(move)
            }
        }

        lastMove = move
        return move
    }

    private fun processKingMove(m: Move) {
        val temp = abs(m.from.x - m.to.x)
        val temp2 = abs(m.from.y - m.to.y)

        if (temp == 1 || temp2 == 1) {
            processNormalMove(m)
        } else {
            // Castling Move
            movePiece(board, m.from, m.to)

            // Move The Rook
            if (m.piece.color == Color.WHITE) {
                if (m.to.x == 6 && m.to.y == 0) {
                    // Castling with the right rook
                    movePiece(board, Point(7, 0), Point(5, 0))
                } else if (m.to.x == 2 && m.to.y == 0) {
                    // Castling with the left rook
                    movePiece(board, Point(0, 0), Point(3, 0))
                }
            } else {
                if (m.to.x == 6 && m.to.y == 7) {
                    // Castling with the left (From black perspective) rook
                    movePiece(board, Point(7, 7), Point(5, 7))
                } else if (m.to.x == 2 && m.to.y == 7) {
                    // Castling with the right (From black perspective) rook
                    movePiece(board, Point(0, 7), Point(3, 7))
                }
            }

        }
    }

    private fun processPawnMove(move: Move) {
        val piece = move.piece

        if (move.from.x == move.to.x || board[move.to.y][move.to.x] != null) {
            /* Basic legal forward move OR Capture Move */
            /* TODO Return the captured piece (Returned from processNormalMove call)*/
            processNormalMove(move)
            /* TODO check for pawn promotion */
        } else {
            /* En Passant move */

            /* TODO Return the captured piece */

            // Capture the opponent piece
            if (piece.color == Color.WHITE) {
                board[move.to.y - 1][move.to.x] = null
            } else {
                board[move.to.y + 1][move.to.x] = null
            }

            // Put the pawn in the new place
            board[move.to.y][move.to.x] = piece
        }


        /* Remove the pawn from the previous place */
        board[move.from.y][move.from.x] = null
    }


    private fun processNormalMove(move: Move) {
        if (board[move.to.y][move.to.x] != null) {
            /* TODO Return the captured piece! */
        }
        movePiece(board, move.from, move.to)
    }
}