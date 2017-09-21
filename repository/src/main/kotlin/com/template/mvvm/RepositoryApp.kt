package com.template.mvvm

import android.support.multidex.MultiDexApplication
import com.template.mvvm.data.source.remote.LicensesApi
import com.template.mvvm.data.source.remote.ProductsApi

abstract class RepositoryApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        // Begin injection here.
        with(Injection(this)) {
            ProductsApi.service = provideProductsApiService()
            LicensesApi.service = provideLicensesApiService()
        }
    }
}