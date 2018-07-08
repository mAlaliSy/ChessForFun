package com.malalisy.chessforfun

import com.malalisy.chessforfun.pieces.*
import com.malalisy.chessforfun.utils.*
import kotlin.math.abs

class RulesManager(val board: Array<Array<Piece?>>) {

    fun isLegalMove(m: Move, lastMove: Move?): Boolean {
        val piece = m.piece
        if (!basicValidation(m))
            return false
        when (piece) {
            is Pawn -> return validatePawnMove(m, lastMove)
            is Knight -> return validateKnightMove(m)
            is Rook -> return validateHorizontalVerticalMove(m)
            is Bishop -> return validateDiagonalMove(m)
            is Queen -> return validateHorizontalVerticalMove(m) || validateDiagonalMove(m)
            is King -> return validateKingMove(m)
        }

        return false
    }

    private fun validateKingMove(m: Move): Boolean {
        val temp = abs(m.x1 - m.x2)
        val temp2 = abs(m.y1 - m.y2)

        // Vertical move: difference of y = 1
        // Horizontal move: difference of x = 1
        // Diagonal move: difference of x = 1 and y = 1
        if (temp == 1 || temp2 == 1)
            return true

        if (!(m.piece as King).isFirstMove) {
            // Not the first move and the difference of vertical and horizontal axis is not 1
            return false
        } else {
            // Check if it is a castling move
            return isValidCastlingMove(board, m)
        }
    }

    private fun validateDiagonalMove(m: Move): Boolean {
        if (abs(m.y2 - m.y1) != abs(m.x2 - m.x1))
        // Not a diagonal move
            return false

        // Check if there is a piece on the way from starting point to end point
        // Get the dir of moving
        val xDir = (m.x2 - m.x1) / abs(m.x2 - m.x1)
        val yDir = (m.y2 - m.y1) / abs(m.y2 - m.y1)
        if (getNearestPieceUntil(board, m.x1, m.y1, m.x2, m.y2, xDir, yDir) != null) {
            // There is a piece blocking the way!
            return false
        }

        return true

    }

    private fun validateHorizontalVerticalMove(m: Move): Boolean {
        if (!(m.x1 == m.x2 || m.y1 == m.y2))
        // Not horizontal or vertical move
            return false

        val xDir: Int
        if (m.x1 == m.x2)
            xDir = 0
        else
            xDir = (m.x2 - m.x1) / abs(m.x2 - m.x1)

        val yDir: Int
        if (m.y1 == m.y2)
            yDir = 0
        else
            yDir = (m.y2 - m.y1) / abs(m.y2 - m.y1)

        if (getNearestPieceUntil(board, m.x1, m.y1, m.x2, m.y2, xDir, yDir) != null)
            return false

        return true
    }

    private fun validateKnightMove(m: Move): Boolean {
        return abs(m.x2 - m.x1) == 2 && abs(m.y2 - m.y1) == 1
                || abs(m.x2 - m.x1) == 1 && abs(m.y2 - m.y1) == 2
    }

    private fun basicValidation(move: Move): Boolean {
        if (move.x1 == move.x2 && move.y1 == move.y2)
            return false
        if (!validPosition(move.x1, move.y1) || !validPosition(move.x2, move.y2))
            return false
        if (board[move.y2][move.x2] != null && move.piece.color == board[move.y2][move.x2]?.color)
            return false

        var nBoard = copyBoard(board)
        nBoard[move.y2][move.x2] = board[move.y1][move.x1]
        nBoard[move.y1][move.x1] = null

        return isKingInCheck(nBoard, move.piece.color)
    }

    private fun validatePawnMove(m: Move, lastMove: Move?): Boolean {
        val piece = m.piece as Pawn
        if (piece.color == Color.WHITE) {

            // Basic one step or tow steps forward move
            if (m.y2 == m.y1 + 1 || piece.isFirstMove && m.y2 == m.y1 + 2 && board[m.y2][m.x2] == null)
                return true
            // Capturing
            if (m.y2 == m.y1 + 1 && (m.x2 == m.x1 + 1 || m.x2 == m.x1 - 1))
                if (board[m.y2][m.x2] != null && board[m.y2][m.x2]?.color != Color.WHITE)
                    return true
            // En passant
            if (lastMove != null && lastMove.piece is Pawn
                    && lastMove.y2 == lastMove.y1 - 2 && m.y1 == lastMove.y2 // Check if the last move was tow step move of a pawn
                    && m.y2 == lastMove.y2 + 1 && m.x2 == lastMove.x2 // Checking if the pawn has the right to En passant
            ) {
                return true
            }

        } else {

            // Basic one step or tow steps forward move
            if (m.y2 == m.y1 - 1 || piece.isFirstMove && m.y2 == m.y1 - 2 && board[m.y2][m.x2] == null)
                return true
            // Capturing
            if (m.y2 == m.y1 - 1 && (m.x2 == m.x1 + 1 || m.x2 == m.x1 - 1))
                return board[m.y2][m.x2] != null && board[m.y2][m.x2]?.color != Color.WHITE
            // En passant
            if (lastMove != null && lastMove.piece is Pawn
                    && lastMove.y2 == lastMove.y1 + 2 && m.y1 == lastMove.y2 // Check if the last move was tow step move of a pawn
                    && m.y2 == lastMove.y2 - 1 && m.x2 == lastMove.x2 // Checking if the pawn has the right to En passant
            ) {
                return true
            }

        }

        return false
    }


}