package com.malalisy.chessforfun

import com.malalisy.chessforfun.pieces.*
import com.malalisy.chessforfun.utils.movePiece
import kotlin.math.abs

class GameController(var board: Array<Array<Piece?>>, var lastMove: Move?) {
    val rulesManager: RulesManager = RulesManager(board)


    fun move(x: Int, y: Int, x2: Int, y2: Int): Move? {
        if (board[y][x] == null) {
            return null
        }

        val move = Move(x, y, x2, y2, board[y][x]!!)

        if (rulesManager.isLegalMove(move, lastMove)) {
            return null
        }
        if (board[y][x] == null)
            return null

        when (board[y][x]) {

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
        val temp = abs(m.x1 - m.x2)
        val temp2 = abs(m.y1 - m.y2)

        if (temp == 1 || temp2 == 1) {
            processNormalMove(m)
        } else {
            // Castling Move
            movePiece(board, m.x1, m.y1, m.x2, m.y2)

            // Move The Rook
            if (m.piece.color == Color.WHITE) {
                if (m.x2 == 6 && m.y2 == 0) {
                    // Castling with the right rook
                    movePiece(board,7, 0, 5, 0)
                } else if (m.x2 == 2 && m.y2 == 0) {
                    // Castling with the left rook
                    movePiece(board,0, 0, 3, 0)
                }
            } else {
                if (m.x2 == 6 && m.y2 == 7) {
                    // Castling with the left (From black perspective) rook
                    movePiece(board, 7, 7, 5, 7)
                } else if (m.x2 == 2 && m.y2 == 7) {
                    // Castling with the right (From black perspective) rook
                    movePiece(board, 0, 7, 3, 7)
                }
            }

        }
    }

    private fun processPawnMove(move: Move) {
        val piece = move.piece

        if (move.x1 == move.x2 || board[move.y2][move.x2] != null) {
            /* Basic legal forward move OR Capture Move */
            /* TODO Return the captured piece (Returned from processNormalMove call)*/
            processNormalMove(move)
            /* TODO check for pawn promotion */
        } else {
            /* En Passant move */

            /* TODO Return the captured piece */

            // Capture the opponent piece
            if (piece.color == Color.WHITE) {
                board[move.y2 - 1][move.x2] = null
            } else {
                board[move.y2 + 1][move.x2] = null
            }

            // Put the pawn in the new place
            board[move.y2][move.x2] = piece
        }


        /* Remove the pawn from the previous place */
        board[move.y1][move.x1] = null
    }


    private fun processNormalMove(move: Move) {
        if (board[move.y2][move.x2] != null) {
            /* TODO Return the captured piece! */
        }
        movePiece(board, move.x1, move.y1, move.x2, move.y2)
    }


}