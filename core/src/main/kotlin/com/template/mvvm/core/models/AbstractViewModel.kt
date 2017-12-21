package com.template.mvvm.core.models

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.experimental.Job

abstract class AbstractViewModel : ViewModel() {
    open fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner) = true
    protected val vmJob = Job()
    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
        Log.d(this.javaClass.name, "${this.javaClass.name}::onCleared")
    }
}