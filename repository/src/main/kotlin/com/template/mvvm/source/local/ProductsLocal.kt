package com.template.mvvm.source.local

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.source.local.entities.products.ImageEntity
import com.template.mvvm.source.local.entities.products.ProductEntity
import io.reactivex.Single

class ProductsLocal : ProductsDataSource {

    override fun getAllProducts(localOnly: Boolean) = DB.INSTANCE.productDao().run {
        getProductList()
                .flatMap({
                    val v: List<Product> = (mutableListOf<Product>()).apply {
                        it.forEach { add(Product.from(it, getImages(it.pid))) }
                        LL.d("products loaded from db")
                    }
                    Single.just(v)
                })
    }

    override fun filterProduct(keyword: String, localOnly: Boolean) = DB.INSTANCE.productDao().run {
        filterProductList(keyword)
                .flatMap({
                    val v: List<Product> = (mutableListOf<Product>()).apply {
                        it.forEach { add(Product.from(it, getImages(it.pid))) }
                        LL.d("filtered $keyword products and loaded from db")
                    }
                    Single.just(v)
                })
    }


    override fun saveProducts(source: List<Product>) = source.apply {
        DB.INSTANCE.productDao().apply {
            insertProducts(source.map { ProductEntity.from(it) })
            LL.w("products write to db")
            source.forEach {
                insertImages(
                        it.pictures.map {
                            ImageEntity.from(it.value)
                        }
                )
            }
            LL.w("products images write to db")
        }

    }
}
