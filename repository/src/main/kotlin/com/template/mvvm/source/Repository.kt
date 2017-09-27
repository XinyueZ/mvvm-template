package com.template.mvvm.source

import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Completable

class Repository(private val licensesRepository: LicensesDataSource, private val productsRepository: ProductsDataSource) : LicensesDataSource, ProductsDataSource {
    // Application's libraries, licenses...
    override fun getAllLibraries(source: LibraryList) = licensesRepository.getAllLibraries(source)
    override fun saveListOfLibrary(source: LibraryList) = licensesRepository.saveListOfLibrary(source)

    // Products, brands, logo....
    override fun getAllProducts(source: ProductList): Completable = productsRepository.getAllProducts(source)
    override fun saveListOfProduct(source: ProductList) = productsRepository.saveListOfProduct(source)

    // Other...
    override fun clear() {
        licensesRepository.clear()
        productsRepository.clear()
    }
}