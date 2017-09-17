package com.template.mvvm.life

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.utils.LL
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class LifeViewModel(context: Application) : AndroidViewModel(context) {
    open fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner) = true
    private val compositeDisposable = CompositeDisposable()
    protected fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        LL.d("${this.javaClass.name}::onCleared")
    }
}