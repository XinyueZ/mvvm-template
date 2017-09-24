package com.template.mvvm.contract

import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Completable

interface ProductsDataSource : DataSource {
    fun getAllProducts(source: ProductList): Completable
    fun saveListOfProduct(listOfProduct: List<Product>): Completable = Completable.complete()
}