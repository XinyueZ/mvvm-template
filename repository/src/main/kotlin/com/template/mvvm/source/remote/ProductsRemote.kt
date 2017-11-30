package com.template.mvvm.source.remote

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

class ProductsRemote : ProductsDataSource {

    override suspend fun getAllProducts(job: Job, localOnly: Boolean) = produce(job) {
        ProductsApi.service.getArticles().execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.run {
                LL.d("products loaded from net")
                send(products.map {
                    Product.from(it, listOf(metaData.category.localizedId))
                })
            } ?: kotlin.run { send(null) }
        } ?: kotlin.run { send(null) }
    }

    override suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean) = produce(job) {
        ProductsApi.service.filterArticles(keyword).execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.run {
                LL.d("filtered $keyword products and loaded from net")
                send(products.map {
                    Product.from(it, listOf(metaData.category.localizedId))
                })
            } ?: kotlin.run { send(null) }
        } ?: kotlin.run { send(null) }
    }
}