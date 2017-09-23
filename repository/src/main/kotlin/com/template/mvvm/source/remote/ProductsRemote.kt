package com.template.mvvm.data.source.remote

import android.arch.lifecycle.LifecycleOwner
import android.net.Uri
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.data.feeds.products.ProductData
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class ProductsRemote : ProductsDataSource {
    private var productList: ProductList? = null

    override fun getAllProducts(lifecycleOwner: LifecycleOwner): Single<ProductList> {
        productList = productList ?: ProductList()
        val ret: Single<ProductList> = Single.zip(Single.just(productList), ProductsApi.service?.getArticles()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread()), BiFunction({ p1, p2 ->
            p1.apply {
                value = (arrayListOf<Product>()).apply {
                    p2.products.forEach {
                        add(Product(
                                it.name,
                                String.format("%s//%s//%s", it.brand.name, it.genders.joinToString(), it.ageGroups.joinToString()),
                                it.media.images.first().largeHdUrl,
                                it.brand.logo ?: Uri.EMPTY))
                    }
                }
            }
        }))
        ret.doFinally({
            clear()
        })
        return ret
    }

    override fun addProduct(productData: ProductData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        productList = null
    }
}