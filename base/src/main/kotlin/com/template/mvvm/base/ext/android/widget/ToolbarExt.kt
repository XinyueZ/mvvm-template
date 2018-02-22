package com.template.mvvm.base.ext.android.widget

import android.support.v7.widget.Toolbar
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