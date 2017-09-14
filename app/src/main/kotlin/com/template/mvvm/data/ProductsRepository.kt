package com.template.mvvm.data

import com.template.mvvm.data.cache.ProductsCache
import com.template.mvvm.data.domain.ProductList
import com.template.mvvm.data.local.ProductsLocal
import com.template.mvvm.data.remote.ProductsRemote
import com.template.mvvm.utils.LL

class ProductsRepository(private val remote: ProductsDataSource = ProductsRemote(),
                         private val local: ProductsDataSource = ProductsLocal(),
                         private val cache: ProductsDataSource = ProductsCache()
) : ProductsDataSource {

    override fun getAllProducts(): ProductList {
        LL.d("ProductsRepository::getAllProducts")
        return remote.getAllProducts()
    }
}
