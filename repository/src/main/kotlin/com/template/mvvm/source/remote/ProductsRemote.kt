package com.template.mvvm.source.remote

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

class ProductsRemote : ProductsDataSource {

    override suspend fun getAllProducts(job: Job, localOnly: Boolean) = produce(job) {
        ProductsApi.service.getArticles().execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.let {
                LL.d("products loaded from net")
                send(it.products.map {
                    Product.from(it)
                })
            }
        }
    }

    override suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean) = produce(job) {
        ProductsApi.service.filterArticles(keyword).execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.let {
                LL.d("filtered $keyword products and loaded from net")
                send(it.products.map {
                    Product.from(it)
                })
            }
        }
    }

    override suspend fun getAllBrands(job: Job, localOnly: Boolean) = produce(job) {
        ProductsApi.service
                .getBrands().execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.let {
                LL.d("brands loaded from net")
                send(it.brands.map {
                    Brand.from(it)
                })
            }
        }
    }
}