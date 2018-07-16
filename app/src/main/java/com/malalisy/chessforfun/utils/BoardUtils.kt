package com.malalisy.chessforfun.utils

import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.Move
import com.malalisy.chessforfun.pojos.Point
import com.malalisy.chessforfun.pojos.pieces.*
import kotlin.math.abs

fun validPosition(x: Int, y: Int) = x in 0..7 && y in 0..7

fun copyBoard(board: Array<Array<Piece?>>): Array<Array<Piece?>> {
    var cboard = Array<Array<Piece?>>(8, {
        Array<Piece?>(8, { index ->
            board[it][index]
        })
    })
    return cboard

}

fun movePiece(board: Array<Array<Piece?>>, p1: Point, p2: Point) {
    board[p2.y][p2.x] = board[p1.y][p1.x]
    board[p1.y][p1.x] = null
}

fun movePiece(board: Array<Array<Piece?>>, move: com.malalisy.chessforfun.pojos.Move) {
    movePiece(board, move.from, move.to)
}


private fun getKingPosition(board: Array<Array<Piece?>>, playerColor: PlayerColor): Point {
    for (i in 0..7) {
        for (j in 0..7) {
            if (board[j][i] != null && board[j][i] is King && board[j][i]?.playerColor == playerColor) {
                return Point(i, j)
            }
        }
    }
    return Point(0, 0)
}

fun isKingInCheck(board: Array<Array<Piece?>>, playerColor: PlayerColor): Boolean {
    val kingPosition = getKingPosition(board, playerColor)
    return getPieceCanMoveTo(board, kingPosition, playerColor.opposite(), null).isNotEmpty()
}

fun isCheckMate(board: Array<Array<Piece?>>, playerColor: PlayerColor, lastMove: com.malalisy.chessforfun.pojos.Move?): Boolean {
    val kingPosition = getKingPosition(board, playerColor)
    val king = board[kingPosition.y][kingPosition.x]

    /*Check if the the king is in check */
    var piecesCanAttack = getPieceCanMoveTo(board, kingPosition, playerColor.opposite(), null)
    if (piecesCanAttack.isEmpty()) {
        /*
        * Not in check
        * */
        return false
    }

    /* Check if it is single check, find a piece that can defend the king */
    if (piecesCanAttack.size == 1) {
        val piecePoint = piecesCanAttack[0].first
        /*Check if there is a piece that can take the piece attacking the king*/
        if (getPieceCanMoveTo(board, piecePoint, playerColor, null).isNotEmpty()) {
            return false
        }

        /* Check if there is a piece can defend the king
         * (Can be putted between the king and the attacking piece if it is not a knight)
         * */
        val piece = piecesCanAttack[0]
        if (piece.second !is Knight) {

            val xDir = if (piece.first.x == kingPosition.x) 0 else (piece.first.x - kingPosition.x) / abs(piece.first.x - kingPosition.x)
            val yDir = if (piece.first.y == kingPosition.y) 0 else (piece.first.y - kingPosition.y) / abs(piece.first.y - kingPosition.y)

            var i = kingPosition.x
            var j = kingPosition.y

            do {
                i += xDir
                j += yDir

                if (getPieceCanMoveTo(board, Point(i, j), playerColor, lastMove).isNotEmpty()) {
                    return false
                }
            } while (i != piece.first.x || j != piece.first.y)
        }
    }

    for (dir in kingDirArray) {
        if (isLegalMove(board, com.malalisy.chessforfun.pojos.Move(kingPosition, kingPosition + dir, king!!), null)) {
            return false
        }
    }
    return true
}

fun getPieceCanAttack(board: Array<Array<Piece?>>, point: Point, playerColor: PlayerColor, lastMove: com.malalisy.chessforfun.pojos.Move?): List<Pair<Point, Piece>> {
    val copyBoard = copyBoard(board)
    copyBoard[point.y][point.x] = Pawn(playerColor.opposite())
    return getPieceCanMoveTo(copyBoard, point, playerColor, lastMove)
}

fun getPieceCanMoveTo(board: Array<Array<Piece?>>, point: Point, playerColor: PlayerColor, lastMove: com.malalisy.chessforfun.pojos.Move?): List<Pair<Point, Piece>> {
    val list = ArrayList<Pair<Point, Piece>>()

    /* Pawns that can move to this position */
    if (playerColor == PlayerColor.WHITE) {


        /*
        * Attacking move
        * */
        if (isPawn(board, Point(point.x + 1, point.y - 1), playerColor)
                && board[point.y][point.x] != null && board[point.y][point.x]!!.playerColor != playerColor
                /*
                * En passant move
                * */
                || canEnPassant(board, Point(point.x + 1, point.y - 1), lastMove, -1)) {
            list.add(Pair(Point(point.x + 1, point.y - 1), board[point.y - 1][point.x + 1]!!))
        }


        /*
        * Attacking move
        * */
        if (isPawn(board, Point(point.x - 1, point.y - 1), playerColor)
                && board[point.y][point.x] != null && board[point.y][point.x]!!.playerColor != playerColor
                /*
                * En passant move
                * */
                || canEnPassant(board, Point(point.x - 1, point.y - 1), lastMove, 1)) {
            list.add(Pair(Point(point.x - 1, point.y - 1), board[point.y - 1][point.x - 1]!!))
        }


        /*
        * Forward move
        * */
        if (isPawn(board, Point(point.x, point.y - 1), playerColor) &&
                board[point.y][point.x] == null) {
            list.add(Pair(Point(point.x, point.y - 1), board[point.y - 1][point.x]!!))
        }

        /*
        * Two steps forward move
        * */
        if (point.y == 3 &&
                isPawn(board, Point(point.x, 1), playerColor) && board[3][point.x] == null) {
            list.add(Pair(Point(point.x, 1), board[1][point.x]!!))
        }


    } else {
        /*
        * Attacking move
        * */
        if (isPawn(board, Point(point.x + 1, point.y + 1), playerColor)
                && board[point.y][point.x] != null && board[point.y][point.x]!!.playerColor != playerColor
                /*
                * En passant move
                * */
                || canEnPassant(board, Point(point.x + 1, point.y + 1), lastMove, -1)) {
            list.add(Pair(Point(point.x + 1, point.y + 1), board[point.y + 1][point.x + 1]!!))
        }

        /*
        * Attacking move
        * */
        if (isPawn(board, Point(point.x - 1, point.y + 1), playerColor)
                && board[point.y][point.x] != null && board[point.y][point.x]!!.playerColor != playerColor
                /*
                * En passant move
                * */
                || canEnPassant(board, Point(point.x - 1, point.y + 1), lastMove, 1)) {
            list.add(Pair(Point(point.x - 1, point.y + 1), board[point.y + 1][point.x - 1]!!))
        }

        /*
        * Forward move
        * */
        if (isPawn(board, Point(point.x, point.y + 1), playerColor) &&
                board[point.y][point.x] == null) {
            list.add(Pair(Point(point.x, point.y + 1), board[point.y + 1][point.x]!!))
        }

        /*
        * Two steps forward move
        * */
        if (point.y == 4 &&
                isPawn(board, Point(point.x, 6), playerColor) && board[4][point.x] == null) {
            list.add(Pair(Point(point.x, 6), board[6][point.x]!!))
        }
    }

    for (dir in knightDirArray) {
        if (isKnight(board, point + dir, playerColor))
            list.add(Pair(point + dir, board[point.y + dir.y][point.x + dir.x]!!))
    }

    // Check for horizontal & vertical
    for (dir in horiVertDirArray) {
        val horiPiece = getNearestPiece(board, point, dir.x, dir.y)

        if (horiPiece != null) {
            /*There is a piece, check if it is queen or rook*/
            if (horiPiece.second.playerColor == playerColor && (horiPiece.second is Queen || horiPiece.second is Rook))
                list.add(horiPiece)
        }

    }

    // Check for diagonal
    for (dir in diagonalDirArray) {
        val nearestDiagonalPiece = getNearestPiece(board, point, dir.x, dir.y)
        if (nearestDiagonalPiece != null) {
            // There is a piece, check if it is bishop or queen
            if (nearestDiagonalPiece.second.playerColor == playerColor && (nearestDiagonalPiece.second is Bishop || nearestDiagonalPiece.second is Queen))
                list.add(nearestDiagonalPiece)
        }
    }

    return list
}

fun getNearestPiece(board: Array<Array<Piece?>>, point: Point, horizontalIncrement: Int, verticalIncrement: Int): Pair<Point, Piece>? {
    var i = point.x
    var j = point.y
    do {
        i += horizontalIncrement
        j += verticalIncrement

        if (!validPosition(i, j))
            return null

        if (board[j][i] != null)
            return Pair(Point(i, j), board[j][i]!!)
    } while (true)
}


fun getNearestPieceUntil(board: Array<Array<Piece?>>, x1: Int, y1: Int, x2: Int, y2: Int, horizontalIncrement: Int, verticalIncrement: Int): Piece? {
    var i = x1
    var j = y1
    do {
        i += horizontalIncrement
        j += verticalIncrement

        if (i == x2 && j == y2)
            return null

        if (board[j][i] != null)
            return board[j][i]
    } while (true)
}

// Check if there is knight on this position
fun isKnight(board: Array<Array<Piece?>>, point: Point, playerColor: PlayerColor) = validPosition(point.x, point.y) && board[point.y][point.x] != null && board[point.y][point.x] is Knight && board[point.y][point.x]?.playerColor == playerColor

// Check if there is pawn on this position
fun isPawn(board: Array<Array<Piece?>>, point: Point, playerColor: PlayerColor) = validPosition(point.x, point.y) && board[point.y][point.x] != null && board[point.y][point.x] is Pawn && board[point.y][point.x]?.playerColor == playerColor

fun canEnPassant(board: Array<Array<Piece?>>, pawnPosition: Point, lastMove: com.malalisy.chessforfun.pojos.Move?, xDir: Int): Boolean {
    if (!validPosition(pawnPosition.x, pawnPosition.y))
        return false
    if (board[pawnPosition.y][pawnPosition.x] == null)
        return false

    if (board[pawnPosition.y][pawnPosition.x]!!.playerColor == PlayerColor.WHITE) {
        /*
        * Check if the last move was tow step move of a pawn
        * */
        return lastMove != null && lastMove.piece is Pawn && lastMove.to.y == lastMove.from.y - 2
                /*
                * Check if the pawn has the right to En passant
                * */
                && pawnPosition.y == lastMove.to.y && (pawnPosition.x + xDir == lastMove.from.x ||
                pawnPosition.x + xDir == lastMove.from.x)
    } else {
        /*
        * Check if the last move was tow step move of a pawn
        * */
        return lastMove != null && lastMove.piece is Pawn && lastMove.to.y == lastMove.from.y + 2
                /*
                * Check if the pawn has the right to En passant
                * */
                && pawnPosition.y == lastMove.to.y && (pawnPosition.x + xDir == lastMove.from.x ||
                pawnPosition.x + xDir == lastMove.from.x)

    }

}