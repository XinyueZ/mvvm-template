package com.template.mvvm.source

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {
    override fun getAllProducts() = Flowable.create<List<Product>>({ emitter ->
        emitter.onNext(local.getAllProducts().blockingFirst())
        emitter.onNext(local.saveProducts(remote.getAllProducts().blockingFirst()))
    }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    override fun saveProducts(source: List<Product>) = local.saveProducts(source)
    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}