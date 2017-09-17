package com.template.mvvm.data.repository

import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import com.template.mvvm.data.domain.products.ProductList
import com.template.mvvm.data.repository.cache.ProductsCache
import com.template.mvvm.data.repository.local.ProductsLocal
import com.template.mvvm.data.repository.remote.ProductsRemote
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class ProductsRepository(private val remote: ProductsDataSource = ProductsRemote(),
                         private val local: ProductsDataSource = ProductsLocal(),
                         private val cache: ProductsDataSource = ProductsCache()
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
