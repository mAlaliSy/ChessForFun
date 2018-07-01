package com.malalisy.chessforfun

import com.malalisy.chessforfun.pieces.*
import kotlin.math.abs

class RulesManager(val board: Array<Array<Piece?>>) {

    fun isLegalMove(m: Move, lastMove: Move): Boolean {
        val piece = m.piece
        if (!basicValidation(m))
            return false
        when {
            piece is Pawn -> return validatePawnMove(m, lastMove)
            piece is Knight -> return validateKnightMove(m)
            piece is Rook -> return validateHorizontalVerticalMove(m)
            piece is Bishop -> return validateDiagonalMove(m)
            piece is Queen -> return validateHorizontalVerticalMove(m) || validateDiagonalMove(m)
            piece is King -> return validateKingMove(m)
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
            if (m.piece.color == Color.WHITE) {
                return if (m.x2 == 6 && m.y2 == 0) {
                    // Castling with the right rook

                    // Check if the right rook also has not moved
                    board[7][0] != null && board[7][0] is Rook && (board[7][0] as Rook).isFirstMove
                } else if (m.x2 == 2 && m.y2 == 0) {
                    // Castling with the left rook

                    // Check if the left rook also has not moved
                    board[0][0] != null && board[0][0] is Rook && (board[0][0] as Rook).isFirstMove
                } else {
                    // Not a castling move either
                    false
                }
            } else {
                return if (m.x2 == 6 && m.y2 == 7) {
                    // Castling with the left (From black perspective) rook

                    // Check if the right rook also has not moved
                    board[7][7] != null && board[7][7] is Rook && (board[7][7] as Rook).isFirstMove
                } else if (m.x2 == 2 && m.y2 == 7) {
                    // Castling with the right (From black perspective) rook

                    // Check if the left rook also has not moved
                    board[7][0] != null && board[7][0] is Rook && (board[7][0] as Rook).isFirstMove
                } else {
                    // Not a castling move either
                    false
                }
            }
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
        if (getNearestPieceUntil(m.x1, m.y1, m.x2, m.y2, xDir, yDir) != null) {
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

        if (getNearestPieceUntil(m.x1, m.y1, m.x2, m.y2, xDir, yDir) != null)
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
        if (board[move.x2][move.y2] != null && move.piece.color == board[move.x2][move.y2]?.color)
            return false

        var nBoard = board.copyOf()
        nBoard[move.x2][move.y2] = board[move.x1][move.y1]
        nBoard[move.x1][move.y1] = null

        return isKingThreaten(nBoard, move.piece.color)
    }

    private fun validatePawnMove(m: Move, lastMove: Move): Boolean {
        val piece = m.piece as Pawn
        if (piece.color == Color.WHITE) {

            // Basic one step or tow steps forward move
            if (m.y2 == m.y1 + 1 || piece.isFirstMove && m.y2 == m.y1 + 2 && board[m.x2][m.y2] == null)
                return true
            // Capturing
            if (m.y2 == m.y1 + 1 && (m.x2 == m.x1 + 1 || m.x2 == m.x1 - 1))
                if (board[m.x2][m.y2] != null && board[m.x2][m.y2]?.color != Color.WHITE)
                    return true
            // En passant
            if (lastMove.piece is Pawn
                    && lastMove.y2 == lastMove.y1 - 2 && m.y1 == lastMove.y2 // Check if the last move was tow step move of a pawn
                    && m.y2 == lastMove.y2 + 1 && m.x2 == lastMove.x2 // Checking if the pawn has the right to En passant
            ) {
                return true
            }

        } else {

            // Basic one step or tow steps forward move
            if (m.y2 == m.y1 - 1 || piece.isFirstMove && m.y2 == m.y1 - 2 && board[m.x2][m.y2] == null)
                return true
            // Capturing
            if (m.y2 == m.y1 - 1 && (m.x2 == m.x1 + 1 || m.x2 == m.x1 - 1))
                return board[m.x2][m.y2] != null && board[m.x2][m.y2]?.color != Color.WHITE
            // En passant
            if (lastMove.piece is Pawn
                    && lastMove.y2 == lastMove.y1 + 2 && m.y1 == lastMove.y2 // Check if the last move was tow step move of a pawn
                    && m.y2 == lastMove.y2 - 1 && m.x2 == lastMove.x2 // Checking if the pawn has the right to En passant
            ) {
                return true
            }

        }

        return false
    }

    fun isKingThreaten(board: Array<Array<Piece?>>, color: Color): Boolean {
        // Get the king's position
        var x = 0
        var y = 0

        for (i in 0..7) {
            for (j in 0..7) {
                if (board[i][j] != null && board[i][j] is King && board[i][j]?.color == color) {
                    x = i
                    y = j
                    break
                }
            }
        }

        // Check for pawn threatening
        if (color == Color.WHITE) {
            if (validPosition(x + 1, y + 1)) {
                val rightDiagonal = board[x + 1][y + 1]
                if (rightDiagonal != null && rightDiagonal is Pawn && rightDiagonal.color != color) {
                    return true
                }
            }
            if (validPosition(x - 1, y + 1)) {
                val leftDiagonal = board[x - 1][y + 1]
                if (leftDiagonal != null && leftDiagonal is Pawn && leftDiagonal.color != color) {
                    return true
                }
            }
        } else {
            if (validPosition(x + 1, y - 1)) {
                val rightDiagonal = board[x + 1][y - 1]
                if (rightDiagonal != null && rightDiagonal is Pawn && rightDiagonal.color != color) {
                    return true
                }
            }
            if (validPosition(x - 1, y - 1)) {
                val leftDiagonal = board[x - 1][y - 1]
                if (leftDiagonal != null && leftDiagonal is Pawn && leftDiagonal.color != color) {
                    return true
                }
            }
        }


        // Check for knight threatening
        val xKDirArray = intArrayOf(-2, -2, 2, 2, -1, 1, -1, 1)
        val yKDirArray = intArrayOf(-1, 1, -1, 1, -2, -2, 2, 2)
        for (i in 0..xKDirArray.size) {
            for (j in 0..yKDirArray.size) {
                if (knightThreatening(x + i, y + j, color))
                    return true
            }
        }


        // Check for horizontal
        val xDirArray = intArrayOf(0, 0, 1, -1)
        val yDirArray = intArrayOf(1, -1, 0, 0)

        for (inX in xDirArray) {
            for (inY in yDirArray) {

                var horiPiece = getNearestPiece(x, y, inX, inY)

                if (horiPiece != null) {
                    // There is a piece, check if it is queen or rook of the opponent
                    // If so, the king is under threat
                    // If not, there is a piece of other types, king is safe even if there is
                    // rook or queen behind this piece, so stop
                    if (horiPiece.color != color && (horiPiece is Queen || horiPiece is Rook))
                        return true
                }

            }
        }

        // Check for diagonal threatening
        val xBDirArray = intArrayOf(1, 1, -1, -1)
        val yBDirArray = intArrayOf(-1, 1, -1, 1)

        for (inX in xBDirArray) {
            for (inY in yBDirArray) {
                val nearestDiagonalPiece = getNearestPiece(x, y, inX, inY)
                if (nearestDiagonalPiece != null) {
                    // There is a piece, check if it is bishop of the opponent
                    // If so, the king is under threat
                    // If not, there is a piece of other types, king is safe even if there is
                    // bishop or queen behind this piece, so stop
                    if (nearestDiagonalPiece.color != color && (nearestDiagonalPiece is Bishop || nearestDiagonalPiece is Queen))
                        return true
                }
            }
        }
        return false
    }

    // Check if there is knight on this position
    private fun knightThreatening(x: Int, y: Int, color: Color) = validPosition(x, y) && board[x][y] != null && board[x][y] is Knight && board[x][y]?.color != color


    private fun getNearestPiece(x: Int, y: Int, horizontalIncrement: Int, verticalIncrement: Int): Piece? {
        var i = x
        var j = y
        do {
            i += horizontalIncrement
            j += verticalIncrement

            if (!validPosition(i, j))
                return null

            if (board[i][j] != null)
                return board[i][j]
        } while (true)
    }


    private fun getNearestPieceUntil(x1: Int, y1: Int, x2: Int, y2: Int, horizontalIncrement: Int, verticalIncrement: Int): Piece? {
        var i = x1
        var j = y1
        do {
            i += horizontalIncrement
            j += verticalIncrement

            if (i == x2 && j == y2)
                return null

            if (board[i][j] != null)
                return board[i][j]
        } while (true)
    }


}