package com.template.mvvm.source.local

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import com.template.mvvm.source.local.entities.products.BrandEntity
import com.template.mvvm.source.local.entities.products.ProductEntity
import io.reactivex.Single

class ProductsLocal : ProductsDataSource {

    override fun getAllProducts(localOnly: Boolean) = DB.INSTANCE.productDao()
            .getProductList()
            .flatMap({
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    it.forEach {
                        this.add(it.toProduct())
                    }
                    LL.d("products loaded from db")
                }
                Single.just(v)
            })

    override fun filterProduct(keyword: String, localOnly: Boolean) = DB.INSTANCE.productDao()
            .filterProductList(keyword)
            .flatMap({
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    it.forEach {
                        this.add(it.toProduct())
                    }
                    LL.d("filtered $keyword products and loaded from db")
                }
                Single.just(v)
            })

    override fun getAllBrands(localOnly: Boolean) = DB.INSTANCE.productDao()
            .getBrandList()
            .flatMap({
                val v: List<Brand> = (mutableListOf<Brand>()).apply {
                    it.forEach {
                        this.add(it.toBrand())
                    }
                    LL.d("brands loaded from db")
                }
                Single.just(v)
            })

    override fun saveProducts(source: List<Product>) = source.apply {
        DB.INSTANCE.apply {
            forEach {
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

    override fun saveBrands(source: List<Brand>) = source.apply {
        DB.INSTANCE.apply {
            forEach {
                productDao().insertBrand(
                        BrandEntity.from(it)
                )
            }
        }
        LL.w("brands write to db")
    }

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}