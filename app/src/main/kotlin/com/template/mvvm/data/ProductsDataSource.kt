package com.template.mvvm.data

import com.template.mvvm.data.domain.ProductList

interface ProductsDataSource {
    fun getAllProducts(): ProductList
}