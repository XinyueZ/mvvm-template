package com.template.mvvm.core.arch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.template.mvvm.base.utils.LL
import com.template.mvvm.core.ViewModelInjection
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.cancel

abstract class AbstractViewModel : ViewModel(), LifecycleObserver {
    protected val bgContext = ViewModelInjection.bgContext
    protected val uiContext by lazy {
        ViewModelInjection.uiContext + CoroutineExceptionHandler({ _, e ->
            onUiJobError(e)
            LL.d(e.message ?: "")
        })
    }
    internal lateinit var lifecycleOwner: LifecycleOwner

    override fun onCleared() {
        super.onCleared()
        bgContext.cancel()
        uiContext.cancel()
        Log.d(this.javaClass.name, "${this.javaClass.name}::onCleared")
    }

    protected open fun onUiJobError(e: Throwable) = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onLifecycleCreate() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onLifecycleStart() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onLifecycleResume() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onLifecyclePause() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onLifecycleStop() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onLifecycleDestroy() = Unit
}

fun AbstractViewModel?.registerLifecycleOwner(owner: LifecycleOwner?) {
    this?.let { vm ->
        owner?.let {
            lifecycleOwner = it
            lifecycleOwner.lifecycle.addObserver(vm)
        }
    }
}