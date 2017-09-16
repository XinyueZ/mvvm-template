package com.template.mvvm.data.repository

import com.template.mvvm.data.cache.ProductsCache
import com.template.mvvm.data.domain.products.ProductList
import com.template.mvvm.data.local.ProductsLocal
import com.template.mvvm.data.remote.ProductsRemote
import io.reactivex.Single

class ProductsRepository(private val remote: ProductsDataSource = ProductsRemote(),
                         private val local: ProductsDataSource = ProductsLocal(),
                         private val cache: ProductsDataSource = ProductsCache()
) : ProductsDataSource {

    override fun getAllProducts(): Single<ProductList> {
        return remote.getAllProducts()
    }
}
