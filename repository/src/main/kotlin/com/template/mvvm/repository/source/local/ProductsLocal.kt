package com.template.mvvm.repository.source.local

import com.template.mvvm.base.utils.LL
import com.template.mvvm.repository.contract.INVALID_PID
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.products.Image
import com.template.mvvm.repository.domain.products.Product
import com.template.mvvm.repository.domain.products.ProductCategory
import com.template.mvvm.repository.domain.products.ProductDetail
import com.template.mvvm.repository.source.local.entities.products.ImageEntity
import com.template.mvvm.repository.source.local.entities.products.ProductCategoryEntity
import com.template.mvvm.repository.source.local.entities.products.ProductEntity
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.produce
import kotlin.coroutines.experimental.CoroutineContext

class ProductsLocal : ProductsDataSource {

    override suspend fun getAllProducts(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean) = produce(coroutineContext) {
        DB.INSTANCE.productDao().apply {
            LL.d("From $offset the products loaded from db")
            send(getProductList(offset).map {
                Product.from(it, getImages(it.pid))
            })
        }
    }

    override suspend fun filterProducts(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean, keyword: String) = produce(coroutineContext) {
        DB.INSTANCE.productDao().apply {
            LL.d("From $offset to be filtered $keyword products and loaded from db")
            send(filterProductList(offset, keyword).map {
                Product.from(it, getImages(it.pid))
            })
        }
    }

    override suspend fun getProductDetail(coroutineContext: CoroutineContext, pid: Long, localOnly: Boolean) = produce(coroutineContext) {
        with(DB.INSTANCE.productDao()) {
            ProductDetail.from(
                    getProduct(pid).first(),
                    getImages(pid)
            ).apply { send(this) }
        }
    }

    override suspend fun getImages(coroutineContext: CoroutineContext, pid: Long) = produce(coroutineContext) {
        DB.INSTANCE.productDao().run {
            takeIf { pid == INVALID_PID }?.let {
                getImages().map { Image.from(it) }
            } ?: kotlin.run {
                getImages(pid).map { Image.from(it) }
            }
        }.also { send(it) }
    }

    override suspend fun saveProducts(coroutineContext: CoroutineContext, source: List<Product>) = produce(coroutineContext) {
        DB.INSTANCE.productDao().apply {
            savePictures(coroutineContext, source)
            insertProducts(
                    source.map {
                        ProductEntity.from(it)
                    }
            )
            send(Unit)
            LL.w("products write to db")
        }
    }

    override suspend fun savePictures(coroutineContext: CoroutineContext, source: List<Product>) = produce(coroutineContext) {
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

    override suspend fun saveProductCategories(coroutineContext: CoroutineContext, source: List<ProductCategory>) = produce(coroutineContext) {
        DB.INSTANCE.productDao().apply {
            insertProductCategories(
                source.map {
                    ProductCategoryEntity.from(it)
                }
            )
            LL.w("product-categories write to db")
            send(Unit)
        }
    }

    override suspend fun deleteAll(coroutineContext: CoroutineContext) = produce(coroutineContext) {
        DB.INSTANCE.productDao().apply {
            val imagesToDelete = async(coroutineContext) { deleteImages() }
            val productsToDelete = async(coroutineContext) { deleteProducts() }
            imagesToDelete.await()
            productsToDelete.await()
            LL.w("deleted products and pictures(images) from db")
            send(Unit)
        }
    }

    override suspend fun deleteAll(coroutineContext: CoroutineContext, keyword: String)= produce(coroutineContext) {
            DB.INSTANCE.productDao().apply {
                val productsToDelete = async(coroutineContext) { getProductList(keyword) }
                LL.w("deleted products from db with $keyword")
                val deletedProducts = productsToDelete.await()
                deletedProducts.forEach {
                    LL.w("deleted image of product from db with $keyword")
                    deleteImages(it.pid)
                }
                deleteProducts(keyword)
                send(Unit)
            }
        }

    override suspend fun getProductCategories(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean) = produce(coroutineContext) {
        DB.INSTANCE.productDao().apply {
            LL.d("The product-categories loaded from db")
            send(getProductCategoryList(offset).map {
                ProductCategory.from(it)
            })
        }
    }

    override suspend fun deleteProductCategories(coroutineContext: CoroutineContext)= produce(coroutineContext) {
        DB.INSTANCE.productDao().apply {
            deleteProductCategories()
            LL.w("deleted product-categories from db")
            send(Unit)
        }
    }
}