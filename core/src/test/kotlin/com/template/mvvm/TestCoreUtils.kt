package com.template.mvvm

import android.content.Context
import android.os.Handler
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowApplication
import org.robolectric.shadows.ShadowLooper
import org.robolectric.util.Scheduler
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

fun advanceToNextPostedRunnable(scheduler: Scheduler = ShadowLooper.getShadowMainLooper().scheduler) {
    scheduler.advanceToNextPostedRunnable()
}

fun advanceToNextPostedRunnable(handler: Handler) {
    advanceToNextPostedRunnable(Shadows.shadowOf(handler.looper).scheduler)
}




