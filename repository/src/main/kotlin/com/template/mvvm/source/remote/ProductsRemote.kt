package com.template.mvvm.source.remote

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import io.reactivex.Flowable

class ProductsRemote : ProductsDataSource {

    override fun getAllProducts(localOnly: Boolean) = ProductsApi.service
            .getArticles()
            .flatMap {
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    it.products.forEach {
                        add(Product(
                                it.pid,
                                it.name,
                                String.format("%s//%s//%s", it.brand.name, it.genders.joinToString(), it.ageGroups.joinToString()),
                                it.media.images.first().largeHdUrl,
                                Brand.from(it.brand)))
                    }
                    LL.d("products loaded from net")
                }
                Flowable.just(v)
            }

    override fun getAllBrands(localOnly: Boolean): Flowable<List<Brand>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}