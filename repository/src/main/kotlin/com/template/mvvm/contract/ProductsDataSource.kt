package com.template.mvvm.contract

import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import io.reactivex.Flowable

interface ProductsDataSource : DataSource {
    fun getAllProducts(localOnly: Boolean = false): Flowable<List<Product>>
    fun filterProduct(keyword: String, localOnly: Boolean = true): Flowable<List<Product>>
    fun getAllBrands(localOnly: Boolean = false): Flowable<List<Brand>>
    fun saveProducts(source: List<Product>): List<Product> = source
    fun saveBrands(source: List<Brand>): List<Brand> = source
}