package com.template.mvvm.base.utils

import android.content.Context
import android.util.DisplayMetrics
import androidx.core.hardware.display.DisplayManagerCompat

data class Size(val width: Int = 0, val height: Int = 0)

@androidx.annotation.Size
fun getScreenSize(cxt: Context?, displayIndex: Int = 0) =
    cxt?.let {
        val displaymetrics = DisplayMetrics()
        val displays = DisplayManagerCompat.getInstance(it)
            .displays
        val display = displays[displayIndex]
        display.getMetrics(displaymetrics)
        Size(displaymetrics.widthPixels, displaymetrics.heightPixels)
    } ?: run { Size() }

