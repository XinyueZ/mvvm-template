package com.template.mvvm.source.remote

import android.net.Uri
import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import io.reactivex.Flowable

class ProductsRemote : ProductsDataSource {

    override fun getAllProducts() = ProductsApi.service
            .getArticles()
            .flatMap {
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    it.products.forEach {
                        add(Product(
                                it.name,
                                String.format("%s//%s//%s", it.brand.name, it.genders.joinToString(), it.ageGroups.joinToString()),
                                it.media.images.first().largeHdUrl,
                                it.brand.logo ?: Uri.EMPTY))
                    }
                    LL.d("products loaded from net")
                }
                Flowable.just(v)
            }

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}