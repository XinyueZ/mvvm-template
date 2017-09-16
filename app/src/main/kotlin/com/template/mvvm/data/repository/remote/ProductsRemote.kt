package com.template.mvvm.data.remote

import com.template.mvvm.data.domain.products.Product
import com.template.mvvm.data.domain.products.ProductList
import com.template.mvvm.data.repository.ProductsDataSource
import com.template.mvvm.data.repository.remote.ProductsApi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductsRemote : ProductsDataSource {
    private val productList = ProductList()
    override fun getAllProducts(): Single<ProductList> {
        return ProductsApi.service.getArticles().observeOn(Schedulers.io()).flatMap {
            val list = arrayListOf<Product>()

            val ret: Single<ProductList> = Single.create({ emitter ->
                with(productList) {
                    if (!emitter.isDisposed)
                        emitter.onSuccess(this)
                }
            })


            it.products.forEach {
                val product = Product(it.name, it.name + "----" + it.name)
                list.add(product)
            }

            productList.value = list

            ret
        }.subscribeOn(AndroidSchedulers.mainThread())
    }
}