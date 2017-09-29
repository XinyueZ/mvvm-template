package com.template.mvvm.contract

import com.template.mvvm.domain.products.Product
import io.reactivex.Flowable

interface ProductsDataSource : DataSource {
    fun getAllProducts(localOnly: Boolean = false): Flowable<List<Product>>
    fun saveProducts(source: List<Product>): List<Product> = source
}