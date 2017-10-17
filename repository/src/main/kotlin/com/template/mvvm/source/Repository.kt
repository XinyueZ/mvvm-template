package com.template.mvvm.source

import android.app.Application
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.licenses.Library
import kotlinx.coroutines.experimental.Job

class Repository(private val licensesRepository: LicensesDataSource, private val productsRepository: ProductsDataSource) : LicensesDataSource, ProductsDataSource {
    // Application's libraries, licenses...
    override suspend fun getAllLibraries(job: Job, localOnly: Boolean) = licensesRepository.getAllLibraries(job, localOnly)

    override suspend fun getLicense(app: Application, job: Job, library: Library, localOnly: Boolean) = licensesRepository.getLicense(app, job, library, localOnly)

    // Products, brands, logo....
    override suspend fun getAllProducts(job: Job, localOnly: Boolean) = productsRepository.getAllProducts(job, localOnly)

    override suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean) = productsRepository.filterProduct(job, keyword, localOnly)

    override suspend fun getAllBrands(job: Job, localOnly: Boolean) = productsRepository.getAllBrands(job, localOnly)

    // Other...
    override fun clear(): Boolean {
        licensesRepository.clear()
        productsRepository.clear()
        return true
    }
}