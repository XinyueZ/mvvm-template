package com.template.mvvm.data.repository.remote

import com.template.mvvm.data.domain.products.Product
import com.template.mvvm.data.domain.products.ProductList
import com.template.mvvm.data.repository.ProductsDataSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class ProductsRemote : ProductsDataSource {
    private val productList = ProductList()
    override fun getAllProducts(): Single<ProductList> {
        return Single.just(productList).doFinally {
            ProductsApi.service.getArticles().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(Consumer {
                val list = arrayListOf<Product>()
                it.products.forEach {
                    val product = Product(it.name, it.name + "----" + it.name)
                    list.add(product)
                }
                productList.value = list
            })
        }
    }
}