package com.template.mvvm.source.local

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductDetail
import com.template.mvvm.source.local.entities.products.ImageEntity
import com.template.mvvm.source.local.entities.products.ProductEntity
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

class ProductsLocal : ProductsDataSource {

    override suspend fun getAllProducts(job: Job, localOnly: Boolean) = produce(job) {
        DB.INSTANCE.productDao().apply {
            LL.d("products loaded from db")
            send(getProductList().map {
                Product.from(it, getImages(it.pid))
            })
        }
    }

    override suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean) = produce(job) {
        DB.INSTANCE.productDao().apply {
            LL.d("filtered $keyword products and loaded from db")
            send(filterProductList(keyword).map {
                Product.from(it, getImages(it.pid))
            })
        }
    }

    override suspend fun getProductDetail(job: Job, pid: String, localOnly: Boolean) = produce(job) {
        with(DB.INSTANCE.productDao()) {
            ProductDetail.from(
                    getProduct(pid).first(),
                    getImages(pid)
            ).apply { send(this) }
        }
    }

    override suspend fun saveProducts(job: Job, source: List<Product>) = produce(job) {
        DB.INSTANCE.productDao().apply {
            savePictures(job, source)
            insertProducts(
                    source.map {
                        ProductEntity.from(it)
                    }
            )
            send(Unit)
            LL.w("products write to db")
        }
    }

    override suspend fun savePictures(job: Job, source: List<Product>) = produce(job) {
        DB.INSTANCE.productDao().apply {
            source.forEach {
                insertImages(
                        it.pictures.map {
                            ImageEntity.from(it.value)
                        }
                )
            }
            send(Unit)
            LL.w("products write pictures(images) to db")
        }
    }
}