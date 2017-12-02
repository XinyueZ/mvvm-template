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
    override suspend fun getAllProducts(job: Job, offset: Int, localOnly: Boolean) = productsRepository.getAllProducts(job, offset, localOnly)

    override suspend fun filterProducts(job: Job, offset: Int, localOnly: Boolean, keyword: String) = productsRepository.filterProducts(job, offset, localOnly, keyword)

    override suspend fun getProductDetail(job: Job, pid: Long, localOnly: Boolean) = productsRepository.getProductDetail(job, pid, localOnly)

    override suspend fun deleteAll(job: Job) = productsRepository.deleteAll(job)

    override suspend fun deleteAll(job: Job, keyword: String) = productsRepository.deleteAll(job, keyword)

    // Other...
    override fun clear() {
        licensesRepository.clear()
        productsRepository.clear()
    }
}