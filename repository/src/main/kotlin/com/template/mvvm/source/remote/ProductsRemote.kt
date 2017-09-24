package com.template.mvvm.source.remote

import android.net.Uri
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class ProductsRemote : ProductsDataSource {

    override fun getAllProducts(source: ProductList): Completable {
        val ret: Single<ProductList> = Single.zip(Single.just(source), ProductsApi.service?.getArticles()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread()), BiFunction({ p1, p2 ->
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
        return ret.toCompletable()
    }

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}