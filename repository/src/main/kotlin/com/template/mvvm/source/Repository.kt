package com.template.mvvm.source

import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.domain.products.Product

class Repository(private val licensesRepository: LicensesDataSource, private val productsRepository: ProductsDataSource) : LicensesDataSource, ProductsDataSource {
    // Application's libraries, licenses...
    override fun getAllLibraries(source: LibraryList) = licensesRepository.getAllLibraries(source)

    override fun saveLibraries(source: LibraryList) = licensesRepository.saveLibraries(source)

    // Products, brands, logo....
    override fun getAllProducts() = productsRepository.getAllProducts()

    override fun saveProducts(source: List<Product>) = productsRepository.saveProducts(source)

    // Other...
    override fun clear() {
        licensesRepository.clear()
        productsRepository.clear()
    }
}