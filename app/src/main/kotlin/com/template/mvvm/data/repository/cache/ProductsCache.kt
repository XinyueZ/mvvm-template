package com.template.mvvm.data.cache

import com.template.mvvm.data.repository.ProductsDataSource
import com.template.mvvm.data.domain.products.ProductList
import io.reactivex.Single

class ProductsCache : ProductsDataSource {

    override fun getAllProducts(): Single<ProductList> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}