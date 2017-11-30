package com.template.mvvm.contract

import com.template.mvvm.domain.products.Product
import io.reactivex.Single

interface ProductsDataSource : DataSource {
    fun getAllProducts(localOnly: Boolean = true): Single<List<Product>> = Single.never()
    fun filterProduct(keyword: String, localOnly: Boolean = true): Single<List<Product>> = Single.never()
    fun saveProducts(source: List<Product>): List<Product> = source
}