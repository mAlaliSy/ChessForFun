package com.malalisy.chessforfun.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

/*
* Avoid instantiating rect every call
* */
val rect = Rect()

fun drawCenter(canvas: Canvas, top: Float, left: Float, paint: Paint, text: String) {
    paint.textAlign = Paint.Align.LEFT
    paint.getTextBounds(text, 0, text.length, rect)
    val x = left - rect.width() / 2f - rect.left
    val y = top + rect.height() / 2f - rect.bottom
    canvas.drawText(text, x, y, paint)
}