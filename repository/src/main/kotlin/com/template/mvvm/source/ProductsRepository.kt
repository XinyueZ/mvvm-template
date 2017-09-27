package com.template.mvvm.source

import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Completable

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {
    override fun getAllProducts(source: ProductList): Completable {
        return local.getAllProducts(source)
                .andThen(remote.getAllProducts(source))
                .andThen(local.saveProducts(source)).doOnDispose {
            LL.i("products disposed")
        }
    }

    override fun saveProducts(source: ProductList) = local.saveProducts(source)
    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}
