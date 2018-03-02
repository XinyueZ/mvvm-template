package com.template.mvvm.core

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.Context
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.template.mvvm.base.utils.LL
import com.template.mvvm.repository.RepositoryInjection
import com.template.mvvm.repository.RepositoryModule

class CoreModule(context: Context) : LifecycleObserver {
    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        RepositoryModule(context)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCoreCreate() {
        LL.d("process on_create")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onCoreStart() {
        LL.d("process on_start")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onCoreResume() {
        LL.d("process on_resume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onCorePause() {
        LL.d("process on_pause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onCoreStop() {
        LL.d("process on_stop")

        ViewModelFactory.destroyInstance()
        RepositoryInjection.destroyInstance()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onCoreDestroy() {
        LL.d("process on_destroy")
    }
}

@GlideModule
class ExAppGlideModule : AppGlideModule()