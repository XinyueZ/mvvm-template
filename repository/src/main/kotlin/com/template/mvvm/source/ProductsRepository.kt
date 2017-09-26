package com.template.mvvm.source

import android.util.Log
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductList
import com.template.mvvm.source.remote.interceptors.MissingNetworkConnectionException
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {
    private val compositeDisposable = CompositeDisposable()
    override fun getAllProducts(source: ProductList) = remote.getAllProducts(source)
            .doOnComplete {
                Log.e("ProductsRepository", "doOnComplete")
                source.value?.let {
                    compositeDisposable.add(local.saveListOfProduct(it).subscribe())
                }
            }
            .doOnDispose {
                compositeDisposable.clear()
            }
            .onErrorResumeNext {
                Log.e("ProductsRepository", "onErrorResumeNext")
                when (it) {
                    is MissingNetworkConnectionException -> local.getAllProducts(source)
                    else -> Completable.complete()
                }
            }

    override fun saveListOfProduct(listOfProduct: List<Product>) = local.saveListOfProduct(listOfProduct)
    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}
