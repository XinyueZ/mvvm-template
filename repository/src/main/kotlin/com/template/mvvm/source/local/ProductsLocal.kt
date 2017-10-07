package com.template.mvvm.source.local

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import com.template.mvvm.source.local.entities.products.BrandEntity
import com.template.mvvm.source.local.entities.products.ProductEntity
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

class ProductsLocal : ProductsDataSource {

    override suspend fun getAllProducts(job: Job, localOnly: Boolean) = produce<List<Product>>(job) {
        mutableListOf<Product>().apply {
            DB.INSTANCE.productDao().getProductList().forEach {
                this.add(it.toProduct())
            }
            LL.d("products loaded from db")
            send(this)
        }
    }

    override suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean) = produce<List<Product>>(job) {
        mutableListOf<Product>().apply {
            DB.INSTANCE.productDao().filterProductList(keyword).forEach {
                this.add(it.toProduct())
            }
            LL.d("filtered $keyword products and loaded from db")
            send(this)
        }
    }

    override suspend fun getAllBrands(job: Job, localOnly: Boolean) = produce<List<Brand>>(job) {
        mutableListOf<Brand>().apply {
            DB.INSTANCE.productDao().getBrandList().forEach {
                this.add(it.toBrand())
            }
            LL.d("brands loaded from db")
            send(this)
        }
    }

    override suspend fun saveProducts(job: Job, source: List<Product>) = produce<Unit>(job) {
        DB.INSTANCE.apply {
            productDao().deleteBrands()
            productDao().deleteProducts()
            source.forEach {
                productDao().insertProduct(
                        ProductEntity.from(it)
                )
                productDao().insertBrand(
                        BrandEntity.from(it.brand)
                )
            }
        }
        LL.w("products write to db")
    }

    override suspend fun saveBrands(job: Job, source: List<Brand>) = produce<Unit>(job) {
        DB.INSTANCE.apply {
            source.forEach {
                productDao().insertBrand(
                        BrandEntity.from(it)
                )
            }
        }
        LL.w("brands write to db")
    }
}