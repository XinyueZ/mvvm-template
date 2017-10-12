package com.template.mvvm.contract

import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import io.reactivex.Single

interface ProductsDataSource : DataSource {
    fun getAllProducts(localOnly: Boolean = false): Single<List<Product>>
    fun filterProduct(keyword: String, localOnly: Boolean = true): Single<List<Product>>
    fun getAllBrands(localOnly: Boolean = false): Single<List<Brand>>
    fun saveProducts(source: List<Product>): List<Product> = source
    fun saveBrands(source: List<Brand>): List<Brand> = source
}