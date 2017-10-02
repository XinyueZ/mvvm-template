package com.template.mvvm.source

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {

    override fun getAllProducts(localOnly: Boolean) = Flowable.create<List<Product>>({ emitter ->
        val remoteCallAndWrite = { local.saveProducts(remote.getAllProducts().blockingFirst()) }
        if (localOnly) {
            emitter.onNext(local.getAllProducts().blockingFirst().takeIf { it.isNotEmpty() }
                    ?: remoteCallAndWrite()
            )
            return@create
        }
        emitter.onNext(remoteCallAndWrite())
    }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    override fun filterProduct(keyword: String, localOnly: Boolean) = Flowable.create<List<Product>>({ emitter ->
        val remoteCallAndWrite = { local.saveProducts(remote.filterProduct(keyword).blockingFirst()) }
        if (localOnly) {
            emitter.onNext(local.filterProduct(keyword).blockingFirst().takeIf { it.isNotEmpty() }
                    ?: remoteCallAndWrite()
            )
            return@create
        }
        emitter.onNext(remoteCallAndWrite())
    }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getAllBrands(localOnly: Boolean) = Flowable.create<List<Brand>>({ emitter ->
        val remoteCallAndWrite = { local.saveBrands(remote.getAllBrands().blockingFirst()) }
        if (localOnly) {
            emitter.onNext(local.getAllBrands().blockingFirst().takeIf { it.isNotEmpty() }
                    ?: remoteCallAndWrite()
            )
            return@create
        }
        emitter.onNext(remoteCallAndWrite())
    }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    override fun saveProducts(source: List<Product>) = local.saveProducts(source)
    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}
