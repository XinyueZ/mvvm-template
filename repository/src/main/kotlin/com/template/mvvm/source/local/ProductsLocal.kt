package com.template.mvvm.source.local

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Image
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductDetail
import com.template.mvvm.source.local.entities.products.ImageEntity
import com.template.mvvm.source.local.entities.products.ProductEntity
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

class ProductsLocal : ProductsDataSource {

    override suspend fun getAllProducts(job: Job, offset: Int, localOnly: Boolean) = produce(job) {
        DB.INSTANCE.productDao().apply {
            LL.d("From $offset the products loaded from db")
            send(getProductList(offset).map {
                Product.from(it, getImages(it.pid))
            })
        }
    }

    override suspend fun filterProducts(job: Job, offset: Int, localOnly: Boolean, keyword: String) = produce(job) {
        DB.INSTANCE.productDao().apply {
            LL.d("From $offset to be filtered $keyword products and loaded from db")
            send(filterProductList(offset, keyword).map {
                Product.from(it, getImages(it.pid))
            })
        }
    }

    override suspend fun getProductDetail(job: Job, pid: Long, localOnly: Boolean) = produce(job) {
        with(DB.INSTANCE.productDao()) {
            ProductDetail.from(
                    getProduct(pid).first(),
                    getImages(pid)
            ).apply { send(this) }
        }
    }

    override suspend fun getImages(job: Job) = produce(job) {
        DB.INSTANCE.productDao().run {
            getImages().map { Image.from(it) }
        }.also { send(it) }
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

    override suspend fun deleteAll(job: Job) = produce(job) {
        DB.INSTANCE.productDao().apply {
            deleteProducts()
            deleteImages()
            LL.w("deleted products and pictures(images) from db")
            send(Unit)
        }
    }

    override suspend fun deleteAll(job: Job, keyword: String) = produce(job) {
        DB.INSTANCE.productDao().apply {
            deleteProducts(keyword)
            LL.w("deleted products and pictures(images<still in development>) from db with $keyword")
            send(Unit)
        }
    }
}