package com.malalisy.chessforfun.utils

import com.malalisy.chessforfun.pojos.Point

val kingDirArray = arrayOf(Point(-1, -1), Point(-1, 0), Point(-1, 1), Point(0, -1), Point(0, 1), Point(1, -1), Point(1, 0), Point(1, 1))


val diagonalDirArray = arrayOf(Point(1, -1), Point(1, 1), Point(-1, -1), Point(-1, 1))


val horiVertDirArray = arrayOf(Point(0, 1), Point(0, -1), Point(1, 0), Point(-1, 0))


val knightDirArray = arrayOf(Point(-2, -1), Point(-2, 1), Point(2, -1), Point(2, 1), Point(-1, -2), Point(1, -2), Point(-1, 2), Point(1, 2))
