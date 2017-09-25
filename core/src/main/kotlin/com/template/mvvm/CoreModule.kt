package com.template.mvvm

import android.support.multidex.MultiDexApplication
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.template.mvvm.source.local.dao.DB
import com.template.mvvm.source.remote.LicensesApi
import com.template.mvvm.source.remote.ProductsApi

abstract class CoreModule : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        // Begin injection here.
        with(Injection.getInstance(this)) {
            ProductsApi.service = provideProductsApiService()
            LicensesApi.service = provideLicensesApiService()
            DB.INSTANCE = provideDatabase(this@CoreModule)
        }
        RepositoryModule(this)
    }
}

@GlideModule
class ExAppGlideModule : AppGlideModule()