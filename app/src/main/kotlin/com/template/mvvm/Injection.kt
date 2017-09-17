package com.template.mvvm

import android.app.Application
import com.template.mvvm.data.repository.LicensesDataSource
import com.template.mvvm.data.repository.LicensesRepository
import com.template.mvvm.data.repository.ProductsRepository
import com.template.mvvm.data.repository.cache.LicensesCache
import com.template.mvvm.data.repository.cache.ProductsCache
import com.template.mvvm.data.repository.local.LicensesLocal
import com.template.mvvm.data.repository.local.ProductsLocal
import com.template.mvvm.data.repository.remote.LicensesRemote
import com.template.mvvm.data.repository.remote.ProductsRemote

object Injection {

    //Provides repository for products
    fun provideProductsRepository() = ProductsRepository(
            provideRemoteProductsRepository(),
            provideLocalProductsRepository(),
            provideCacheProductsRepository()
    )

    private fun provideRemoteProductsRepository() = ProductsRemote()
    private fun provideLocalProductsRepository() = ProductsLocal()
    private fun provideCacheProductsRepository() = ProductsCache()

    //Provides repository for licenses
    fun provideLicensesRepository(application: Application) = LicensesRepository(
            application,
            provideRemoteLicensesRepository(),
            provideLocalLicensesRepository(application),
            provideCacheLicensesRepository()
    )

    private fun provideRemoteLicensesRepository(): LicensesDataSource = LicensesRemote()
    private fun provideLocalLicensesRepository(application: Application) = LicensesLocal(application)
    private fun provideCacheLicensesRepository() = LicensesCache()
}