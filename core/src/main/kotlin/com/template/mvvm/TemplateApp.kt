package com.template.mvvm

import android.content.Context
import android.net.ConnectivityManager
import android.support.multidex.MultiDexApplication
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

open class TemplateApp : MultiDexApplication() {

    companion object {
        var connectivityManager: ConnectivityManager? = null
    }

    override fun onCreate() {
        super.onCreate()
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}