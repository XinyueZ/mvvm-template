package com.template.mvvm.data.repository.remote

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistryOwner
import android.net.Uri
import com.template.mvvm.data.domain.products.Product
import com.template.mvvm.data.domain.products.ProductList
import com.template.mvvm.data.repository.ProductsDataSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class ProductsRemote : ProductsDataSource {
    private val productList = ProductList()
    override fun getAllProducts(lifecycleOwner: LifecycleOwner): Single<ProductList> {
        return Single.zip(Single.just(productList), ProductsApi.service.getArticles().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()), BiFunction({ p1, p2 ->
            p1.apply {
                value = (arrayListOf<Product>()).apply {
                    p2.products.forEach {
                        add(Product(
                                it.name,
                                String.format("%s//%s//%s", it.brand.name, it.genders.joinToString(), it.ageGroups.joinToString()),
                                Uri.parse(it.media.images.first().largeHdUrl),
                                if (it.brand.logo != null) Uri.parse(it.brand.logo) else Uri.EMPTY))
                    }
                }
            }
        }))
    }
}