package com.template.mvvm.data.remote

import com.template.mvvm.data.ProductsDataSource
import com.template.mvvm.data.domain.products.ProductList
import io.reactivex.Single

class ProductsRemote : ProductsDataSource {
    val list = ProductList()

    override fun getAllProducts(): Single<ProductList> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}