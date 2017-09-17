package com.template.mvvm.data.repository

import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import com.template.mvvm.data.domain.products.ProductList
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {
    private val productList = ProductList()

    override fun getAllProducts(lifecycleOwner: LifecycleRegistryOwner): Single<ProductList> {
        return Single.zip(Single.just(productList), remote.getAllProducts(lifecycleOwner), BiFunction({ p1, p2 ->
            p2.observe(lifecycleOwner, Observer {
                p1.value = it
            })
            productList
        }))
    }
}
