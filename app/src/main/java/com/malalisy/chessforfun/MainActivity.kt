package com.malalisy.chessforfun


import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.malalisy.chessforfun.pojos.Move
import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.Point
import com.malalisy.chessforfun.utils.getInitialBoard
import com.malalisy.chessforfun.views.GameView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GameView.BoardListener, GameController.GameEndCallback, GameController.MoveCallback {

    lateinit var gameController: GameController

    var touchedPoint: Point? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameController = GameController(getInitialBoard(), null, PlayerColor.WHITE, 3, this, this)

        gameView.playerColor = PlayerColor.WHITE
        gameView.board = gameController.board

        gameView.acceptMoves = true

//
//        gameView.setBoardDarkColor(Color.parseColor("#2d9fbf"))
//        gameView.setBoardLightColor(Color.parseColor("#a5cace"))

        gameView.boardListener = this

    }

    override fun onSquareSelected(point: Point) {
        touchedPoint = point
        gameView.highlightAvailableMoves(gameController.getAvailableMoves(point))
    }

    override fun onMoveSelected(to: Point): Boolean {
        if (gameController.move(touchedPoint!!, to) == null) {
            /*
            * TODO: Alert invalid move!
            * */
            return false
        } else {
            gameView.acceptMoves = false
            return true
        }

    }

    override fun onMoveCanceled() {
        touchedPoint = null
    }

    override fun onWin(winnerColor: PlayerColor) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("$winnerColor Won!")
        alertDialog.setPositiveButton("OK", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0?.dismiss()
            }

        })
        alertDialog.show()
    }

    override fun onDraw(drawCause: GameController.DrawCause) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMoveMade(move: Move) {
        gameView.onOpponentPlayed(move)
    }

}
