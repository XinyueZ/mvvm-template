package com.template.mvvm.source.cache

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import io.reactivex.Flowable

class ProductsCache : ProductsDataSource {

    override fun getAllProducts(): Flowable<List<Product>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}