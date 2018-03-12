package com.xch.im.definetest.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object UiUtils {

    fun getScreenWidthPixels(context: Context): Int {
        val dm = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                .getMetrics(dm)
        return dm.widthPixels
    }

    fun dipToPx(context: Context, dip: Int): Int {
        return (dip * getScreenDensity(context) + 0.5f).toInt()
    }

    fun getScreenDensity(context: Context): Float {
        try {
            val dm = DisplayMetrics()
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                    .getMetrics(dm)
            return dm.density
        } catch (e: Exception) {
            return DisplayMetrics.DENSITY_DEFAULT.toFloat()
        }

    }

}
