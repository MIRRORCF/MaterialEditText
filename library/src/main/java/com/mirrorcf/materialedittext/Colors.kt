package com.mirrorcf.materialedittext

import android.graphics.Color
import kotlin.math.sqrt


object Colors {

    fun isLight(color: Int): Boolean {
        return sqrt(
                Color.red(color) * Color.red(color) * .241 + Color.green(color) * Color.green(color) * .691 + Color.blue(color) * Color.blue(color) * .068) > 130
    }
}