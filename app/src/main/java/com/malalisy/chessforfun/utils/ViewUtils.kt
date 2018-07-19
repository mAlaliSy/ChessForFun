package com.malalisy.chessforfun.utils

import com.malalisy.chessforfun.pojos.PlayerColor
import com.malalisy.chessforfun.pojos.Point

fun convertPoint(point: Point, playerColor: PlayerColor): Point {
    val newX = if (playerColor == PlayerColor.WHITE) point.x else 7 - point.x
    val newY = if (playerColor == PlayerColor.WHITE) 7 - point.y else point.y
    return Point(newX, newY)
}