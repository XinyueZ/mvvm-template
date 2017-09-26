package com.template.mvvm.source.local

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import com.template.mvvm.source.local.dao.DB
import com.template.mvvm.source.local.entities.products.ProductEntity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductsLocal : ProductsDataSource {

    override fun getAllProducts(source: ProductList) = DB.INSTANCE.productDao().getProductList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable({
                Completable.create { sub ->
                    with(ArrayList<Product>()) {
                        it.forEach {
                            this.add(it.toProduct())
                        }
                        source.value = this
                        sub.onComplete()
                        return@create
                    }
                }
            })

    override fun saveListOfProduct(listOfProduct: List<Product>) = Completable.create { sub ->
        listOfProduct.forEach {
            DB.INSTANCE.productDao().insertProduct(
                    ProductEntity.from(it)
            )
        }
        sub.onComplete()
        return@create
    }.subscribeOn(Schedulers.io())

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}