package com.template.mvvm.source.cache

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Completable

class ProductsCache : ProductsDataSource {

    override fun getAllProducts(source: ProductList): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}