package com.template.mvvm.contract

import com.template.mvvm.domain.products.ProductList
import io.reactivex.Completable

interface ProductsDataSource : DataSource {
    fun getAllProducts(source: ProductList): Completable
    fun saveProducts(source: ProductList): Completable = Completable.complete()
}