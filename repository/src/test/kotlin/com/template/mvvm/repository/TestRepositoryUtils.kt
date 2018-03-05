package com.template.mvvm.repository

import android.content.Context
import org.robolectric.shadows.ShadowApplication
import java.util.concurrent.TimeUnit

fun context(): Context = ShadowApplication.getInstance().applicationContext


inline fun sleepWhile(interval: Long = 1000L, maxIntervals: Int = 10, crossinline condition: (() -> Boolean)) {
    var loop = 0

    while (condition()) {
        TimeUnit.MILLISECONDS.sleep(interval)

        loop++

        if (loop >= maxIntervals) {
            throw IllegalStateException("Slept longer than ${loop * interval}ms. Abort.")
        }
    }
}



