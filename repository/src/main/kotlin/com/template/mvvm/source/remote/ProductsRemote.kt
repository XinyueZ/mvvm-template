package com.template.mvvm.source.remote

import android.net.Uri
import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductsRemote : ProductsDataSource {

    override fun getAllProducts(source: ProductList) = ProductsApi.service.getArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable({
                Completable.fromAction({
                    with(arrayListOf<Product>()) {
                        it.products.forEach {
                            add(Product(
                                    it.name,
                                    String.format("%s//%s//%s", it.brand.name, it.genders.joinToString(), it.ageGroups.joinToString()),
                                    it.media.images.first().largeHdUrl,
                                    it.brand.logo ?: Uri.EMPTY))
                        }
                        source.value = this
                    }
                    LL.d("products loaded from net")
                })
            })

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}