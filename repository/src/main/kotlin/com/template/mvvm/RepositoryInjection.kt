package com.template.mvvm

import android.annotation.SuppressLint
import android.content.Context
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

class RepositoryInjection private constructor() {
    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: RepositoryInjection? = null

        fun getInstance() =
                INSTANCE ?: synchronized(RepositoryInjection::class.java) {
                    INSTANCE ?: RepositoryInjection()
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

    fun provideRepository(context: Context) = DS_INSTANCE ?: synchronized(this) {
        DS_INSTANCE ?: Repository(provideLicensesRepository(context), provideProductsRepository())
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
    private fun provideLicensesRepository(context: Context) = LicensesRepository(
            context,
            provideRemoteLicensesRepository(),
            provideLocalLicensesRepository(context),
            provideCacheLicensesRepository()
    )

    private fun provideRemoteLicensesRepository(): LicensesDataSource = LicensesRemote()
    private fun provideLocalLicensesRepository(context: Context) = LicensesLocal(context)
    private fun provideCacheLicensesRepository() = LicensesCache()
}