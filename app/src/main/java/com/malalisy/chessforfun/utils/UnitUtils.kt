package com.malalisy.chessforfun.utils

import android.content.res.Resources
import android.util.TypedValue


fun getPixels(unit: Int, size: Float): Int {
    val metrics = Resources.getSystem().displayMetrics
    return TypedValue.applyDimension(unit, size, metrics).toInt()
}

fun getPixelsFromSP(size: Float) = getPixels(TypedValue.COMPLEX_UNIT_SP, size)

fun getPixelsFromDP(size: Float) = getPixels(TypedValue.COMPLEX_UNIT_DIP, size)