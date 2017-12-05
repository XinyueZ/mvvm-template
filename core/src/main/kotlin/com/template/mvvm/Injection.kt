package com.template.mvvm

import android.annotation.SuppressLint
import android.app.Application
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.source.LicensesRepository
import com.template.mvvm.source.ProductsRepository
import com.template.mvvm.source.Repository
import com.template.mvvm.source.cache.LicensesCache
import com.template.mvvm.source.cache.ProductsCache
import com.template.mvvm.source.local.LicensesLocal
import com.template.mvvm.source.local.ProductsLocal
import com.template.mvvm.source.remote.LicensesRemote
import com.template.mvvm.source.remote.ProductsRemote

class Injection private constructor(application: Application) {
    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: Injection? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(Injection::class.java) {
                    INSTANCE ?: Injection(application)
                            .also { INSTANCE = it }
                }

        fun destroyInstance() {
            INSTANCE?.let {
                with(it) {
                    DS_INSTANCE?.clear()
                    DS_INSTANCE = null
                }
                INSTANCE = null
            }
        }
    }

    // Provides whole repository

    @SuppressLint("StaticFieldLeak")
    @Volatile private var DS_INSTANCE: Repository? = null

    fun provideRepository(application: Application) = DS_INSTANCE ?: synchronized(this) {
        DS_INSTANCE ?: Repository(provideLicensesRepository(application), provideProductsRepository())
                .also { DS_INSTANCE = it }
    }

    // Provides repository for products
    private fun provideProductsRepository() = ProductsRepository(
            provideRemoteProductsRepository(),
            provideLocalProductsRepository(),
            provideCacheProductsRepository()
    )

    private fun provideRemoteProductsRepository() = ProductsRemote()
    private fun provideLocalProductsRepository() = ProductsLocal()
    private fun provideCacheProductsRepository() = ProductsCache()

    // Provides repository for licenses
    private fun provideLicensesRepository(application: Application) = LicensesRepository(
            application,
            provideRemoteLicensesRepository(),
            provideLocalLicensesRepository(application),
            provideCacheLicensesRepository()
    )

    private fun provideRemoteLicensesRepository(): LicensesDataSource = LicensesRemote()
    private fun provideLocalLicensesRepository(application: Application) = LicensesLocal(application)
    private fun provideCacheLicensesRepository() = LicensesCache()
}