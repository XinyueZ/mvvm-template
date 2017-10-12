package com.template.mvvm.source.cache

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import io.reactivex.Single

class ProductsCache : ProductsDataSource {

    override fun getAllProducts(localOnly: Boolean): Single<List<Product>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun filterProduct(keyword: String, localOnly: Boolean): Single<List<Product>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllBrands(localOnly: Boolean): Single<List<Brand>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}