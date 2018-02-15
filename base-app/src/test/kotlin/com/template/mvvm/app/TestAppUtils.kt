package com.template.mvvm.app

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.support.annotation.IdRes
import android.view.View
import org.junit.Assert
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.shadows.ShadowApplication
import org.robolectric.shadows.ShadowLooper
import org.robolectric.util.Scheduler
import java.util.concurrent.TimeUnit

fun context(): Context = ShadowApplication.getInstance().applicationContext
fun <T : Activity> ActivityController<T>.finish() {
    try {
        pause().stop().destroy()
    } catch (ignored: Throwable) {
    }
}

fun <T : View> ActivityController<out Activity>.applyView(@IdRes id: Int, block: T.() -> Unit = {}): T
        = get().applyView(id, block)

fun <T : View> Activity.applyView(@IdRes id: Int, block: T.() -> Unit = {}): T =
        findViewById<T>(id).apply {
            Assert.assertNotNull(this)
            block(this)
        }

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