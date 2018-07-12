package com.malalisy.chessforfun

import com.malalisy.chessforfun.pieces.Piece

data class Move(var from: Point, var to: Point, var piece: Piece)