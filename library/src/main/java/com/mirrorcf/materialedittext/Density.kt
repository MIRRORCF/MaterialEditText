package com.mirrorcf.materialedittext

import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt


internal object Density {

    fun dp2px(context: Context, dp: Float): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
        return px.roundToInt()
    }
}