package com.template.mvvm.source

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.contract.select
import com.template.mvvm.domain.products.Product
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {
    private val compositeDisposable = CompositeDisposable()
    private fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun getAllProducts(localOnly: Boolean) = select(
            { addToAutoDispose(it) }, // Disposable control
            { remote.getAllProducts() }, // Fetch remote-data
            { local.saveProducts(it) },  // Save data in DB after fetch remote-data
            { local.getAllProducts() }, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() },// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() },// Last chance when local provides nothing
            localOnly
    )

    override fun filterProduct(keyword: String, localOnly: Boolean) = select(
            { addToAutoDispose(it) }, // Disposable control
            { remote.filterProduct(keyword) }, // Fetch remote-data
            { local.saveProducts(it) },  // Save data in DB after fetch remote-data
            { local.filterProduct(keyword) }, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() },// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() },// Last chance when local provides nothing
            localOnly
    )

    override fun saveProducts(source: List<Product>) = local.saveProducts(source)

    override fun clear() {
        compositeDisposable.clear()
    }
}
