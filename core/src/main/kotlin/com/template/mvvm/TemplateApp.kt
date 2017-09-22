package com.template.mvvm

import android.support.multidex.MultiDexApplication

abstract class TemplateApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        RepositoryModule(this)
    }
}