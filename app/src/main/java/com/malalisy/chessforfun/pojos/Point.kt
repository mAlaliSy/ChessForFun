package com.malalisy.chessforfun.pojos

data class Point(var x: Int, var y: Int) {
    operator fun plus(point: Point) = Point(point.x + x, point.y + y)

    operator fun plus(point: Pair<Int, Int>) = Point(point.first + x, point.second + y)

    override operator fun equals(any: Any?): Boolean {
        if (any == null)
            return false

        if (any is Point) return x == any.x && y == any.y
        if (any is Pair<*, *>) return x == any.first && y == any.second

        return false
    }

    infix fun addToY(y: Int): Point = Point(this.x, this.y + y)
    infix fun addToX(x: Int): Point = Point(this.x + x, this.y)

}
