package com.malalisy.chessforfun

import android.util.Log
import com.malalisy.chessforfun.chess_engine.MoveChooser
import com.malalisy.chessforfun.chess_engine.MovesGenerator
import com.malalisy.chessforfun.pojos.Move
import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.Point
import com.malalisy.chessforfun.pojos.pieces.King
import com.malalisy.chessforfun.pojos.pieces.Pawn
import com.malalisy.chessforfun.pojos.pieces.Piece
import com.malalisy.chessforfun.utils.isCheckMate
import com.malalisy.chessforfun.utils.isLegalMove
import com.malalisy.chessforfun.utils.movePiece
import kotlin.math.abs

class GameController(var board: Array<Array<Piece?>>, var lastMove: com.malalisy.chessforfun.pojos.Move?, val playerColor: PlayerColor, val difficulty: Int, var gameEndCallback: GameEndCallback, var moveCallback: MoveCallback) {

    val moveChooser: MoveChooser

    init {
        moveChooser = MoveChooser(playerColor.opposite())
    }

    fun move(from: Point, to: Point): Move? {
        if (board[from.y][from.x] == null) {
            return null
        }

        val move = Move(from, to, board[from.y][from.x]!!)

        if (!isLegalMove(board, move, lastMove)) {
            return null
        }

        when (board[from.y][from.x]) {

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
        /*
        * TODO: Check if it is draw
        * */
        if (isCheckMate(board, playerColor.opposite(), lastMove)) {
            gameEndCallback.onWin(playerColor)
        } else {
            Thread(Runnable {
                val chosenMove = moveChooser.chooseMove(board, difficulty, move)
                movePiece(board, chosenMove)
                lastMove = chosenMove

                if (isCheckMate(board, playerColor, lastMove))
                    gameEndCallback.onWin(playerColor.opposite())
                moveCallback.onMoveMade(chosenMove)

            }).start()
        }

        return move
    }


    private fun processKingMove(m: com.malalisy.chessforfun.pojos.Move) {
        val temp = abs(m.from.x - m.to.x)
        val temp2 = abs(m.from.y - m.to.y)

        if (temp == 1 || temp2 == 1) {
            processNormalMove(m)
        } else {
            // Castling Move
            movePiece(board, m.from, m.to)

            // Move The Rook
            if (m.piece.playerColor == PlayerColor.WHITE) {
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

    private fun processPawnMove(move: com.malalisy.chessforfun.pojos.Move) {
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
            if (piece.playerColor == PlayerColor.WHITE) {
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


    private fun processNormalMove(move: com.malalisy.chessforfun.pojos.Move) {
        if (board[move.to.y][move.to.x] != null) {
            /* TODO Return the captured piece! */
        }
        movePiece(board, move.from, move.to)
    }

    fun getAvailableMoves(point: Point): List<Point> {
        val moves = MovesGenerator.getAvailableMoves(board, point, lastMove)
        val points = ArrayList<Point>(moves.size)
        moves.forEach { points.add(it.to) }
        return points
    }


    interface GameEndCallback {
        fun onWin(winnerColor: PlayerColor)
        fun onDraw(drawCause: DrawCause)
    }

    enum class DrawCause {
        NO_SUFFICIENT_MATERIALS,
        STALEMATE
    }

    interface MoveCallback {
        fun onMoveMade(move: Move)
    }

}