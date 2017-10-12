package com.template.mvvm.source.remote

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import io.reactivex.Single

class ProductsRemote : ProductsDataSource {

    override fun getAllProducts(localOnly: Boolean) = ProductsApi.service
            .getArticles()
            .flatMap {
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    it.products.forEach {
                        add(Product.from(it))
                    }
                    LL.d("products loaded from net")
                }
                Single.just(v)
            }

    override fun filterProduct(keyword: String, localOnly: Boolean) = ProductsApi.service
            .filterArticles(keyword)
            .flatMap {
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    it.products.forEach {
                        add(Product.from(it))
                    }
                    LL.d("filtered $keyword products and loaded from net")
                }
                Single.just(v)
            }

    override fun getAllBrands(localOnly: Boolean) = ProductsApi.service
            .getBrands()
            .flatMap {
                val v: List<Brand> = (mutableListOf<Brand>()).apply {
                    it.brands.forEach {
                        add(Brand.from(it))
                    }
                    LL.d("brands loaded from net")
                }
                Single.just(v)
            }

}