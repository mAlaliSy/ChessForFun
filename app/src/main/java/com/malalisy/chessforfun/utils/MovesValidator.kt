package com.malalisy.chessforfun.utils

import android.util.Log
import com.malalisy.chessforfun.pojos.Move
import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.Point
import com.malalisy.chessforfun.pojos.pieces.*
import kotlin.math.abs

fun isLegalMove(board: Array<Array<Piece?>>, m: Move, lastMove: Move?): Boolean {

    val piece = m.piece
    if (!basicValidation(board, m)) {
        return false
    }
    when (piece) {
        is Pawn -> return validatePawnMove(board, m, lastMove)
        is Knight -> return validateKnightMove(board, m)
        is Rook -> return validateHorizontalVerticalMove(board, m)
        is Bishop -> return validateDiagonalMove(board, m)
        is Queen -> return validateHorizontalVerticalMove(board, m) || validateDiagonalMove(board, m)
        is King -> return validateKingMove(board, m)
    }

    return false
}

fun willResultInCheck(board: Array<Array<Piece?>>, move: Move): Boolean {
    val nBoard = copyBoard(board)
    nBoard[move.to.y][move.to.x] = board[move.from.y][move.from.x]
    nBoard[move.from.y][move.from.x] = null
    return isKingInCheck(nBoard, move.piece.playerColor)
}

private fun validateKingMove(board: Array<Array<Piece?>>, m: Move): Boolean {
    val temp = abs(m.from.x - m.to.x)
    val temp2 = abs(m.from.y - m.to.y)

    /*
    * Vertical move: difference of y = 1
    * Horizontal move : difference of x = 1
    * Diagonal move : difference of x = 1 and y = 1
    * */
    if (temp <= 1 && temp2 <= 1)
        return true

    if (!(m.piece as King).isFirstMove) {
        // Not the first move and the difference of vertical and horizontal axis is not 1
        return false
    } else {
        // Check if it is a castling move
        return isValidCastlingMove(board, m)
    }
}

private fun validateDiagonalMove(board: Array<Array<Piece?>>, m: Move): Boolean {
    if (abs(m.to.y - m.from.y) != abs(m.to.x - m.from.x))
    // Not a diagonal move
        return false

    // Check if there is a piece on the way from starting point to end point
    // Get the dir of moving
    val xDir = (m.to.x - m.from.x) / abs(m.to.x - m.from.x)
    val yDir = (m.to.y - m.from.y) / abs(m.to.y - m.from.y)
    if (getNearestPieceUntil(board, m.from.x, m.from.y, m.to.x, m.to.y, xDir, yDir) != null) {
        // There is a piece blocking the way!
        return false
    }

    return true

}

private fun validateHorizontalVerticalMove(board: Array<Array<Piece?>>, m: Move): Boolean {
    if (!(m.from.x == m.to.x || m.from.y == m.to.y))
    // Not horizontal or vertical move
        return false

    val xDir = if (m.from.x == m.to.x) 0 else (m.to.x - m.from.x) / abs(m.to.x - m.from.x)

    val yDir = if (m.from.y == m.to.y) 0 else (m.to.y - m.from.y) / abs(m.to.y - m.from.y)

    if (getNearestPieceUntil(board, m.from.x, m.from.y, m.to.x, m.to.y, xDir, yDir) != null)
        return false

    return true
}

private fun validateKnightMove(board: Array<Array<Piece?>>, m: Move): Boolean {
    return abs(m.to.x - m.from.x) == 2 && abs(m.to.y - m.from.y) == 1
            || abs(m.to.x - m.from.x) == 1 && abs(m.to.y - m.from.y) == 2
}

private fun basicValidation(board: Array<Array<Piece?>>, move: Move): Boolean {
    if (move.from == move.to)
        return false
    if (!validPosition(move.from.x, move.from.y) || !validPosition(move.to.x, move.to.y))
        return false
    if (board[move.to.y][move.to.x] != null && move.piece.playerColor == board[move.to.y][move.to.x]?.playerColor)
        return false
    return !willResultInCheck(board, move)
}

private fun validatePawnMove(board: Array<Array<Piece?>>, m: Move, lastMove: Move?): Boolean {
    val piece = m.piece as Pawn
    if (piece.playerColor == PlayerColor.WHITE) {
        // Basic one step or tow steps forward move
        if (m.to.x == m.from.x && (m.to.y == m.from.y + 1 || piece.isFirstMove(m.from.y) && m.to.y == m.from.y + 2) && board[m.to.y][m.to.x] == null) {
            return true
        }
        // Capturing
        if (m.to.y == m.from.y + 1 && (m.to.x == m.from.x + 1 || m.to.x == m.from.x - 1))
            if (board[m.to.y][m.to.x] != null && board[m.to.y][m.to.x]?.playerColor != PlayerColor.WHITE) {
                return true
            }
        // En passant
        if (
        /*
        * Check if the last move was tow step move of a pawn
        * */
                lastMove != null && lastMove.piece is Pawn && lastMove.to.y == lastMove.from.y - 2
                /*
                * Check if the pawn has the right to En passant
                * */
                && m.from.y == lastMove.to.y && (m.from.x == lastMove.from.x - 1 ||
                        m.from.x == lastMove.from.x + 1)
                /*
                * Check if it is an En passant move
                * */
                && m.to.y == lastMove.to.y + 1 && m.to.x == lastMove.to.x
        ) {
            return true
        }

    } else {

        // Basic one step or tow steps forward move
        if (m.from.x == m.to.x && (m.to.y == m.from.y - 1 || piece.isFirstMove(m.from.y) && m.to.y == m.from.y - 2) && board[m.to.y][m.to.x] == null)
            return true
        // Capturing
        if (m.to.y == m.from.y - 1 && (m.to.x == m.from.x + 1 || m.to.x == m.from.x - 1))
            return board[m.to.y][m.to.x] != null && board[m.to.y][m.to.x]?.playerColor != PlayerColor.WHITE
        // En passant

        if (
        /*
        * Check if the last move was tow step move of a pawn
        * */
                lastMove != null && lastMove.piece is Pawn && lastMove.to.y == lastMove.from.y + 2
                /*
                * Check if the pawn has the right to En passant
                * */
                && m.from.y == lastMove.to.y && (m.from.x == lastMove.from.x - 1 ||
                        m.from.x == lastMove.from.x + 1)
                /*
                * Check if it is an En passant move
                * */
                && m.to.y == lastMove.to.y - 1 && m.to.x == lastMove.to.x
        ) {
            return true
        }

    }

    return false
}


fun isValidCastlingMove(board: Array<Array<Piece?>>, m: Move): Boolean {
    // Castling is not allowed if the king is under threat
    if (isKingInCheck(board, m.piece.playerColor)) {
        return false
    }

    if (m.piece.playerColor == PlayerColor.WHITE) {
        if (m.to.x == 6 && m.to.y == 0) {
            // Castling with the right rook

            // Check if there is a piece between the king and the rook
            if (board[0][6] != null || board[0][5] != null)
                return false

            /*
             *   Check that the king does not pass through a square that is
             *   attacked by an enemy piece
             */
            var cBoard = copyBoard(board)
            movePiece(cBoard, m.from, Point(6, 0))
            if (isKingInCheck(cBoard, m.piece.playerColor))
                return false
            cBoard = copyBoard(board)
            movePiece(cBoard, m.from, Point(5, 0))
            if (isKingInCheck(cBoard, m.piece.playerColor))
                return false

            // Check if the right rook also has not moved
            return board[0][7] != null && board[0][7] is Rook && (board[0][7] as Rook).isFirstMove
        } else if (m.to.x == 2 && m.to.y == 0) {
            // Castling with the left rook

            if (board[0][2] != null || board[0][3] != null)
                return false

            /*
             *   Check that the king does not pass through a square that is
             *   attacked by an enemy piece
             */
            var cBoard = copyBoard(board)
            movePiece(cBoard, m.from, Point(2, 0))
            if (isKingInCheck(cBoard, m.piece.playerColor))
                return false
            cBoard = copyBoard(board)
            movePiece(cBoard, m.from, Point(3, 0))
            if (isKingInCheck(cBoard, m.piece.playerColor))
                return false


            // Check if the left rook also has not moved
            return board[0][0] != null && board[0][0] is Rook && (board[0][0] as Rook).isFirstMove
        } else {
            // Not a castling move either
            return false
        }
    } else {
        if (m.to.x == 6 && m.to.y == 7) {
            // Castling with the left (From black perspective) rook

            // Check if there is a piece between the king and the rook
            if (board[7][6] != null || board[7][5] != null)
                return false

            /*
             *   Check that the king does not pass through a square that is
             *   attacked by an enemy piece
             */
            var cBoard = copyBoard(board)
            movePiece(cBoard, m.from, Point(6, 7))
            if (isKingInCheck(cBoard, m.piece.playerColor))
                return false
            cBoard = copyBoard(board)
            movePiece(cBoard, m.from, Point(5, 7))
            if (isKingInCheck(cBoard, m.piece.playerColor))
                return false
            // Check if the right rook also has not moved
            return board[7][7] != null && board[7][7] is Rook && (board[7][7] as Rook).isFirstMove
        } else if (m.to.x == 2 && m.to.y == 7) {
            // Castling with the right (From black perspective) rook

            if (board[7][2] != null || board[7][3] != null)
                return false

            /*
             *   Check that the king does not pass through a square that is
             *   attacked by an enemy piece
             */
            var cBoard = copyBoard(board)
            movePiece(cBoard, m.from, Point(2, 7))
            if (isKingInCheck(cBoard, PlayerColor.BLACK))
                return false
            cBoard = copyBoard(board)
            movePiece(cBoard, m.from, Point(3, 7))
            if (isKingInCheck(cBoard, PlayerColor.BLACK))
                return false


            // Check if the left rook also has not moved
            return board[7][0] != null && board[7][0] is Rook && (board[7][0] as Rook).isFirstMove
        } else {
            // Not a castling move either
            return false
        }
    }

}