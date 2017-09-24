package com.template.mvvm.source

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.ProductList

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {

    override fun getAllProducts(source: ProductList) = remote.getAllProducts(source)

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}
