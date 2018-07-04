package com.malalisy.chessforfun

import com.malalisy.chessforfun.pieces.*

class GameController(var board: Array<Array<Piece?>>, var lastMove: Move?) {
    val rulesManager: RulesManager = RulesManager(board)


    public fun move(x: Int, y: Int, x2: Int, y2: Int): Move? {
        if (board[y][x] == null) {
            return null
        }

        val move = Move(x, y, x2, y2, board[y][x]!!)

        if (rulesManager.isLegalMove(move, lastMove)) {
            return null
        }

        when (board[y][x]) {

            is Pawn -> {
                processPawnMove(move)
            }
            is Rook -> {

            }
            is Knight -> {

            }
            is Bishop -> {

            }
            is Queen -> {

            }
            is King -> {

            }
            else -> {
                return null
            }
        }

        lastMove = move
        return move
    }

    private fun processPawnMove(move: Move) {
        val piece = move.piece

        if (move.x1 == move.x2) {
            /* Basic legal forward move */

            board[move.y2][move.x2] = piece

            /*TODO check for pawn promotion*/

        } else if (board[move.y2][move.x2] != null) {
            /* Normal Capture Move */

            /* TODO check for pawn promotion */

            /* TODO Return the captured piece */

            board[move.y2][move.x2] = piece

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


}