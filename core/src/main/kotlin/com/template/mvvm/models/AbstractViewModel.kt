package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class AbstractViewModel : ViewModel() {
    open fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner) = true
    private val compositeDisposable = CompositeDisposable()
    protected fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        Log.d("${this.javaClass.name}", "${this.javaClass.name}::onCleared")
    }
}

abstract class AbstractItemViewModel : AbstractViewModel() {
    open fun isEmpty() = false
    open fun emptyType() = Int.MIN_VALUE
}