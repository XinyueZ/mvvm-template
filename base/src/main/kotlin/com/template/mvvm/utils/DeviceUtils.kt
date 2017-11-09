package com.template.mvvm.utils

import android.content.Context
import android.support.v4.hardware.display.DisplayManagerCompat
import android.util.DisplayMetrics

data class Size(val width: Int = 0, val height: Int = 0)

@android.support.annotation.Size
fun getScreenSize(cxt: Context?, displayIndex: Int = 0) =
        cxt?.let {
            val displaymetrics = DisplayMetrics()
            val displays = DisplayManagerCompat.getInstance(it)
                    .displays
            val display = displays[displayIndex]
            display.getMetrics(displaymetrics)
            Size(displaymetrics.widthPixels, displaymetrics.heightPixels)
        } ?: run { Size() }

