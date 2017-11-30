package com.template.mvvm.source.remote

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import io.reactivex.Single

class ProductsRemote : ProductsDataSource {

    override fun getAllProducts(localOnly: Boolean) = ProductsApi.service
            .getArticles()
            .flatMap { productsData ->
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    productsData.products.forEach {
                        add(Product.from(it, listOf(productsData.metaData.category.localizedId)))
                    }
                    LL.d("products loaded from net")
                }
                Single.just(v)
            }

    override fun filterProduct(keyword: String, localOnly: Boolean) = ProductsApi.service
            .filterArticles(keyword)
            .flatMap { productsData ->
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    productsData.products.forEach {
                        add(Product.from(it, listOf(productsData.metaData.category.localizedId)))
                    }
                    LL.d("filtered $keyword products and loaded from net")
                }
                Single.just(v)
            }
}