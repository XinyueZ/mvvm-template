package com.template.mvvm.source.remote

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import io.reactivex.Flowable
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

class ProductsRemote : ProductsDataSource {

    override suspend fun getAllProducts(job: Job, localOnly: Boolean) = produce<List<Product>>(job) {
        ProductsApi.service.getArticles().execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.let {
                mutableListOf<Product>().apply {
                    it.products.forEach {
                        add(Product.from(it))
                    }
                    LL.d("products loaded from net")
                    Flowable.just(this)
                }
            }
        }
    }

    override suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean) = produce<List<Product>>(job) {
        ProductsApi.service.filterArticles(keyword).execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.let {
                mutableListOf<Product>().apply {
                    it.products.forEach {
                        add(Product.from(it))
                    }
                    LL.d("filtered $keyword products and loaded from net")
                    send(this)
                }
            }
        }
    }

    override suspend fun getAllBrands(job: Job, localOnly: Boolean) = produce<List<Brand>>(job) {
        ProductsApi.service
                .getBrands().execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.let {
                mutableListOf<Brand>().apply {
                    it.brands.forEach {
                        add(Brand.from(it))
                    }
                    LL.d("brands loaded from net")
                    send(this)
                }
            }
        }
    }
}