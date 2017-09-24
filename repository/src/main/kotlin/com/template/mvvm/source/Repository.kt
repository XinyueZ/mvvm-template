package com.template.mvvm.source

import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Completable

class Repository(private val licensesRepository: LicensesRepository, private val productsRepository: ProductsRepository) : LicensesDataSource, ProductsDataSource {
    // Application's libraries, licenses...
    override fun getAllLibraries(source: LibraryList) = licensesRepository.getAllLibraries(source)
    override fun saveListOfLibrary(listOfLibrary: List<Library>) = licensesRepository.saveListOfLibrary(listOfLibrary)

    // Products, brands, logo....
    override fun getAllProducts(source: ProductList): Completable = productsRepository.getAllProducts(source)
    override fun saveListOfProduct(listOfProduct: List<Product>) = productsRepository.saveListOfProduct(listOfProduct)

    // Other...
    override fun clear() {
        licensesRepository.clear()
        productsRepository.clear()
    }
}