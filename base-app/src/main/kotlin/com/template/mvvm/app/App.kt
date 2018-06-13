package com.template.mvvm.app

import androidx.multidex.MultiDexApplication
import com.template.mvvm.core.CoreModule

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        CoreModule(this)
    }
}

