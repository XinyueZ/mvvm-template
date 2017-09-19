package com.template.mvvm

import android.content.Context
import android.net.ConnectivityManager
import android.support.multidex.MultiDexApplication
import com.template.mvvm.data.source.remote.LicensesApi
import com.template.mvvm.data.source.remote.ProductsApi

open class TemplateApp : MultiDexApplication() {

    companion object {
        var connectivityManager: ConnectivityManager? = null
    }

    override fun onCreate() {
        super.onCreate()
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        ProductsApi.service = Injection.provideProductsApiService()
        LicensesApi.service = Injection.provideLicensesApiService()
    }
}