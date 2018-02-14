package com.template.mvvm.base.ext

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver

fun Lifecycle.putObserver(o: Any?) {
    when (o) {
        is LifecycleObserver -> addObserver(o)
    }
}