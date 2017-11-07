package com.template.mvvm.source

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import io.reactivex.Single
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

    override fun getAllProducts(localOnly: Boolean) = Single.create<List<Product>>({ emitter ->
        val remoteCallAndWrite = {
            addToAutoDispose(remote.getAllProducts().subscribe({
                local.saveProducts(it)
                addToAutoDispose(local.getAllProducts().subscribe({ if (it.isNotEmpty()) emitter.onSuccess(it) }, { emitter.onError(it) }))
            }, { emitter.onError(it) }))
        }
        if (localOnly) {
            addToAutoDispose(local.getAllProducts().subscribe(
            {
                if (it.isNotEmpty()) emitter.onSuccess(it)
                else remoteCallAndWrite()
            }, { emitter.onError(it) }
            ))
            return@create
        }
        remoteCallAndWrite()
    }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

    override fun filterProduct(keyword: String, localOnly: Boolean) = Single.create<List<Product>>({ emitter ->
        val remoteCallAndWrite = {
            addToAutoDispose(remote.filterProduct(keyword).subscribe({
                local.saveProducts(it)
                addToAutoDispose(local.filterProduct(keyword).subscribe({ if (it.isNotEmpty()) emitter.onSuccess(it) }, { emitter.onError(it) }))
            }, { emitter.onError(it) }))
        }
        if (localOnly) {
            addToAutoDispose(local.filterProduct(keyword).subscribe(
            {
                if (it.isNotEmpty()) emitter.onSuccess(it)
                else remoteCallAndWrite()
            }, { emitter.onError(it) }
            ))
            return@create
        }
        remoteCallAndWrite()
    }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

    override fun getAllBrands(localOnly: Boolean) = Single.create<List<Brand>>({ emitter ->
        val remoteCallAndWrite = {
            addToAutoDispose(remote.getAllBrands().subscribe({
                local.saveBrands(it)
                addToAutoDispose(local.getAllBrands().subscribe({ if (it.isNotEmpty()) emitter.onSuccess(it) }, { emitter.onError(it) }))
            }, { emitter.onError(it) }))
        }
        if (localOnly) {
            addToAutoDispose(local.getAllBrands().subscribe(
            {
                if (it.isNotEmpty()) emitter.onSuccess(it)
                else remoteCallAndWrite()
            }, { emitter.onError(it) }
            ))
            return@create
        }
        remoteCallAndWrite()
    }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

    override fun saveProducts(source: List<Product>) = local.saveProducts(source)

    override fun clear() {
        compositeDisposable.clear()
    }
}
