package com.template.mvvm.base.ext.android.arch.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

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