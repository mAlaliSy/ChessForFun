package com.malalisy.chessforfun.utils

import android.content.Context
import android.graphics.Typeface


val ROOT = "fonts/"
val FONTAWESOME = ROOT + "fa-solid-900.ttf";

fun getTypeface(context: Context, font: String): Typeface {
    return Typeface.createFromAsset(context.getAssets(), font);
}

fun getIconTypeFace(context: Context): Typeface {
    return getTypeface(context, FONTAWESOME)
}
