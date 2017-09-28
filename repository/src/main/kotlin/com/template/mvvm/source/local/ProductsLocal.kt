package com.template.mvvm.source.local

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.source.local.dao.DB
import com.template.mvvm.source.local.entities.products.ProductEntity
import io.reactivex.Flowable

class ProductsLocal : ProductsDataSource {

    override fun getAllProducts() = DB.INSTANCE.productDao()
            .getProductList()
            .flatMap({
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    it.forEach {
                        this.add(it.toProduct())
                    }
                    LL.d("products loaded from db")
                }
                Flowable.just(v)
            })

    override fun saveProducts(source: List<Product>) = source.apply {
        forEach {
            DB.INSTANCE.productDao().insertProduct(
                    ProductEntity.from(it)
            )
        }
        LL.w("products write to db")
    }

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}