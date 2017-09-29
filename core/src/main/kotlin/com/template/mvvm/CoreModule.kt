package com.template.mvvm

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import android.support.multidex.MultiDexApplication
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.template.mvvm.source.local.dao.DB
import com.template.mvvm.source.remote.LicensesApi
import com.template.mvvm.source.remote.ProductsApi

abstract class CoreModule : MultiDexApplication(), LifecycleObserver {
    override final fun onCreate() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        super.onCreate()
        RepositoryModule(this)
        onCoreCreate()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCoreCreate() {
        LL.d("process on_create")

        // Begin injection here.
        with(Injection.getInstance(this)) {
            ProductsApi.service = provideProductsApiService()
            LicensesApi.service = provideLicensesApiService()
            DB.INSTANCE = provideDatabase(this@CoreModule)
        }
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