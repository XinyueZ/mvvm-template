package com.template.mvvm.core.models

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.template.mvvm.repository.LL
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI

abstract class AbstractViewModel : ViewModel(), LifecycleObserver {
    protected val vmJob = Job()
    protected val vmUiJob by lazy {
        UI + CoroutineExceptionHandler({ _, e ->
            onUiJobError(e)
            LL.d(e.message ?: "")
        }) + vmJob
    }
    internal lateinit var lifecycleOwner: LifecycleOwner

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
        Log.d(this.javaClass.name, "${this.javaClass.name}::onCleared")
    }

    protected open fun onUiJobError(e: Throwable) = Unit
}

fun AbstractViewModel?.registerLifecycleOwner(owner: LifecycleOwner?) {
    this?.let { vm ->
        owner?.let {
            lifecycleOwner = it
            lifecycleOwner.lifecycle.addObserver(vm)
        }
    }
}