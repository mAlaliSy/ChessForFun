package com.malalisy.chessforfun

import com.malalisy.chessforfun.pieces.Piece

data class Move(var x1:Int, var y1:Int, var x2:Int, var y2:Int, var piece: Piece)