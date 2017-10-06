package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.Job

abstract class AbstractViewModel : ViewModel() {
    open fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner) = true
    protected val vmJob = Job()
    private val compositeDisposable = CompositeDisposable()
    protected fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
        compositeDisposable.clear()
        Log.d("${this.javaClass.name}", "${this.javaClass.name}::onCleared")
    }
}