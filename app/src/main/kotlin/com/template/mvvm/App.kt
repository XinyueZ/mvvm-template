package com.template.mvvm

import android.support.multidex.MultiDexApplication

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        CoreModule(this)
    }
}

