package com.template.mvvm.source.local

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import com.template.mvvm.source.local.dao.DB
import com.template.mvvm.source.local.entities.products.ProductEntity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductsLocal : ProductsDataSource {

    override fun getAllProducts(source: ProductList) = DB.INSTANCE.productDao()
            .getProductList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable ({
                Completable.fromAction({
                    with(ArrayList<Product>()) {
                        it.forEach {
                            this.add(it.toProduct())
                        }
                        source.value = this
                    }
                    LL.d("products loaded from db")
                })
            })

    override fun saveProducts(source: ProductList) = Completable.fromAction({
        source.value?.forEach {
            DB.INSTANCE.productDao().insertProduct(
                    ProductEntity.from(it)
            )
        }
        LL.w("products write to db")
    }).subscribeOn(Schedulers.io())

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}