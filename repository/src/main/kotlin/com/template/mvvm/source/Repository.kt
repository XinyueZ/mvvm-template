package com.template.mvvm.source

import android.app.Application
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.products.Product

class Repository(private val licensesRepository: LicensesDataSource, private val productsRepository: ProductsDataSource) : LicensesDataSource, ProductsDataSource {
    // Application's libraries, licenses...
    override fun getAllLibraries() = licensesRepository.getAllLibraries()

    override fun saveLibraries(source: List<Library>) = licensesRepository.saveLibraries(source)

    override fun getLicense(app: Application, library: Library) = licensesRepository.getLicense(app, library)

    // Products, brands, logo....
    override fun getAllProducts() = productsRepository.getAllProducts()

    override fun saveProducts(source: List<Product>) = productsRepository.saveProducts(source)

    // Other...
    override fun clear() {
        licensesRepository.clear()
        productsRepository.clear()
    }
}