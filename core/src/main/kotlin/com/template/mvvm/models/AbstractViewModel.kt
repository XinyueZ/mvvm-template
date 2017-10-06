package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch

abstract class AbstractViewModel : ViewModel() {
    open fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner) = true
    private val job = Job()
    protected fun <E> startJob(channel: Channel<E>, consumer: suspend (E) -> Unit) = launch(job) {
        channel.consumeEach { consumer(it) }
    }

    private val compositeDisposable = CompositeDisposable()
    protected fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        compositeDisposable.clear()
        Log.d("${this.javaClass.name}", "${this.javaClass.name}::onCleared")
    }
}