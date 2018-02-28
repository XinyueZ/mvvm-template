package com.template.mvvm.base.ext.android.widget

import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.support.v7.graphics.Palette
import android.support.v7.widget.Toolbar
import com.template.mvvm.base.ext.lang.inverted
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.channels.consumeEach

fun Toolbar.onNavigationOnClick(block: suspend () -> Unit) {
    val eventActor = actor<Unit>(
        UI,
        capacity = Channel.CONFLATED
    ) {
        // Handling only most recently received update.
        consumeEach { block() }
    }
    setNavigationOnClickListener {
        eventActor.offer(Unit)
    }
}

fun Toolbar.setPalette(palette: Palette) {
    with(palette) {
        when (swatches.isEmpty()) {
            false -> {
                with(swatches[0]) {
                    rgb.inverted().let { inverted ->
                        navigationIcon?.setColorFilter(
                            inverted,
                            SRC_ATOP
                        ) ?: Unit
                    }
                }
            }
        }
    }
}