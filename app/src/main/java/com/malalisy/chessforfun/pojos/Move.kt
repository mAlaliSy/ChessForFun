package com.malalisy.chessforfun.pojos

import com.malalisy.chessforfun.pojos.pieces.Piece

data class Move(var from: com.malalisy.chessforfun.pojos.Point, var to: com.malalisy.chessforfun.pojos.Point, var piece: Piece)