package com.template.mvvm

import android.app.Application
import com.template.mvvm.data.source.remote.LicensesApi
import com.template.mvvm.data.source.remote.ProductsApi

class RepositoryModule(application: Application) {
    init {
        onCreate(application)
    }

    private fun onCreate(application: Application) {
        // Begin injection here.
        with(Injection(application)) {
            ProductsApi.service = provideProductsApiService()
            LicensesApi.service = provideLicensesApiService()
        }
    }
}