package com.template.mvvm.source.remote

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import kotlinx.coroutines.experimental.channels.produce
import kotlin.coroutines.experimental.CoroutineContext

class ProductsRemote : ProductsDataSource {

    override suspend fun getAllProducts(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean) = produce(coroutineContext) {
        ProductsApi.service.getArticles(offset).execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.run {
                LL.d("From $offset the products loaded from net")
                send(products.map {
                    Product.from(it, listOf(metaData.category.localizedId))
                })
            } ?: kotlin.run { send(null) }
        } ?: kotlin.run { send(null) }
    }

    override suspend fun filterProducts(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean, keyword: String) = produce(coroutineContext) {
        ProductsApi.service.filterArticles(offset, keyword).execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.run {
                LL.d("From $offset to be filtered $keyword products and loaded from net")
                send(products.map {
                    Product.from(it, listOf(metaData.category.localizedId))
                })
            } ?: kotlin.run { send(null) }
        } ?: kotlin.run { send(null) }
    }
}