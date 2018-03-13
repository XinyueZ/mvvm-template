package com.template.mvvm.base.ext.android.arch.lifecycle

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

inline fun <reified T : Any> LiveData<T>?.setupObserve(
    lifecycleOwner: LifecycleOwner?,
    crossinline block: (T.() -> Unit)
) {
    lifecycleOwner?.run {
        this@setupObserve?.apply {
            removeObservers(this@run)
            observe(this@run, Observer {
                if (it != null)
                    block(it)
            })
        }
    }
}