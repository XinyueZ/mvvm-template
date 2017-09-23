package com.template.mvvm.data.source

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.data.feeds.products.ProductData
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {
    private var productList: ProductList? = null

    override fun getAllProducts(lifecycleOwner: LifecycleOwner): Single<ProductList> {
        productList = productList ?: ProductList()
        val ret: Single<ProductList> = Single.zip(Single.just(productList), remote.getAllProducts(lifecycleOwner), BiFunction({ p1, p2 ->
            p2.observe(lifecycleOwner, Observer {
                p1.value = it
            })
            p1
        }))
        ret.doFinally({
            clear()
        })
        return ret
    }

    override fun addProduct(productData: ProductData) {
        local.addProduct(productData)
    }

    override fun clear() {
        productList = null
    }
}
