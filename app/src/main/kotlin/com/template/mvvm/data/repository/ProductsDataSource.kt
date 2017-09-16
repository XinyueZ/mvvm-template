package com.template.mvvm.data.repository

import com.template.mvvm.data.domain.products.ProductList
import io.reactivex.Single

interface ProductsDataSource {
    fun getAllProducts(): Single<ProductList>
}