package com.malalisy.chessforfun


import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.utils.getIconTypeFace
import com.malalisy.chessforfun.utils.getInitialBoard
import com.malalisy.chessforfun.views.GameView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameView.playerPlayerColor = PlayerColor.WHITE
        gameView.board = getInitialBoard()

        gameView.setBoardDarkColor(Color.parseColor("#2d9fbf"))
        gameView.setBoardLightColor(Color.parseColor("#a5cace"))

    }
}
