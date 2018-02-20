package com.template.mvvm.core.ext

import java.lang.ref.Reference

fun <R, T> Reference<T>?.withReferent(run: (T.() -> R?)): R? {
    this?.get()?.apply {
        return run(this)
    }
    return null
}
