package com.template.mvvm.source

import android.app.Application
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.products.Product
import kotlinx.coroutines.experimental.Job

class Repository(private val licensesRepository: LicensesDataSource, private val productsRepository: ProductsDataSource) : LicensesDataSource, ProductsDataSource {
    // Application's libraries, licenses...
    override suspend fun getAllLibraries(job: Job, localOnly: Boolean) = licensesRepository.getAllLibraries(job, localOnly)

    override suspend fun saveLibraries(job: Job, source: List<Library>) = licensesRepository.saveLibraries(job, source)

    override suspend fun getLicense(app: Application, job: Job, library: Library, localOnly: Boolean) = licensesRepository.getLicense(app, job, library, localOnly)

    // Products, brands, logo....
    override fun getAllProducts(localOnly: Boolean) = productsRepository.getAllProducts(localOnly)

    override fun filterProduct(keyword: String, localOnly: Boolean) = productsRepository.filterProduct(keyword, localOnly)

    override fun getAllBrands(localOnly: Boolean) = productsRepository.getAllBrands(localOnly)

    override fun saveProducts(source: List<Product>) = productsRepository.saveProducts(source)

    // Other...
    override fun clear(): Boolean {
        licensesRepository.clear()
        productsRepository.clear()
        return true
    }
}