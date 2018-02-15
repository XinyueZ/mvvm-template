package com.template.mvvm.app

import android.support.multidex.MultiDexApplication
import com.template.mvvm.core.CoreModule

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        CoreModule(this)
    }
}

