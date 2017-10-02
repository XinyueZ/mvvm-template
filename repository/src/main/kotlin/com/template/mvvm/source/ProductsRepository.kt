package com.template.mvvm.source

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {
    private val compositeDisposable = CompositeDisposable()
    private fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun getAllProducts(localOnly: Boolean) = Flowable.create<List<Product>>({ emitter ->
        val remoteCallAndWrite = {
            addToAutoDispose(remote.getAllProducts().subscribe(
                    {
                        local.saveProducts(it)
                    },
                    {}
            ))
        }
        if (localOnly) {
            addToAutoDispose(local.getAllProducts().subscribe(
                    {
                        if(it.isNotEmpty()) emitter.onNext(it)
                        else remoteCallAndWrite()
                    },
                    {}
            ))
            return@create
        }
        remoteCallAndWrite()
    }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    override fun filterProduct(keyword: String, localOnly: Boolean) = Flowable.create<List<Product>>({ emitter ->
        val remoteCallAndWrite = {
            addToAutoDispose(remote.filterProduct(keyword).subscribe(
                    {
                        local.saveProducts(it)
                    },
                    {}
            ))
        }
        if (localOnly) {
            addToAutoDispose(local.filterProduct(keyword).subscribe(
                    {
                        if(it.isNotEmpty()) emitter.onNext(it)
                        else remoteCallAndWrite()
                    },
                    {}
            ))
            return@create
        }
        remoteCallAndWrite()
    }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getAllBrands(localOnly: Boolean) = Flowable.create<List<Brand>>({ emitter ->
        val remoteCallAndWrite = {
            addToAutoDispose(remote.getAllBrands().subscribe(
                    {
                        local.saveBrands(it)
                    },
                    {}
            ))
        }
        if (localOnly) {
            addToAutoDispose(local.getAllBrands().subscribe(
                    {
                        if(it.isNotEmpty()) emitter.onNext(it)
                        else remoteCallAndWrite()
                    },
                    {}
            ))
            return@create
        }
        remoteCallAndWrite()
    }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    override fun saveProducts(source: List<Product>) = local.saveProducts(source)

    override fun clear() {
        compositeDisposable.clear()
    }
}
