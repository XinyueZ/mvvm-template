package com.template.mvvm

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import android.support.multidex.MultiDexApplication
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

abstract class CoreModule : MultiDexApplication(), LifecycleObserver {
    override final fun onCreate() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        super.onCreate()
        RepositoryModule(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCoreCreate() {
        LL.d("process on_create")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onCoreStart() {
        LL.d("process on_start")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onCoreResume() {
        LL.d("process on_resume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected open fun onCorePause() {
        LL.d("process on_pause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onCoreStop() {
        LL.d("process on_stop")

        ViewModelFactory.destroyInstance()
        Injection.destroyInstance()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun onCoreDestroy() {
        LL.d("process on_destroy")
    }
}

@GlideModule
class ExAppGlideModule : AppGlideModule()