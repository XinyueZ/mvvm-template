package com.template.mvvm

import android.app.Application
import com.facebook.stetho.Stetho

class RepositoryModule(application: Application) {
    init {
        onCreate(application)
    }

    private fun onCreate(application: Application) {
        Stetho.initializeWithDefaults(application)
    }
}