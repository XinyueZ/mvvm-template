package com.template.mvvm

import android.support.multidex.MultiDexApplication

abstract class CoreModule : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        RepositoryModule(this)
    }
}