package com.template.mvvm

import android.app.Application
import com.facebook.stetho.Stetho
import com.template.mvvm.source.local.dao.DB
import com.template.mvvm.source.remote.LicensesApi
import com.template.mvvm.source.remote.ProductsApi

class RepositoryModule(application: Application) {
    init {
        onCreate(application)
    }

    private fun onCreate(application: Application) {
        Stetho.initializeWithDefaults(application)
        // Begin injection here.
        with(Injection(application)) {
            ProductsApi.service = provideProductsApiService()
            LicensesApi.service = provideLicensesApiService()
            DB.INSTANCE = provideDatabase(application)
        }
    }
}