package com.template.mvvm.core.models.product

import com.template.mvvm.repository.contract.ProductsDataSource
import kotlin.coroutines.experimental.CoroutineContext

abstract class FilterViewModel(repository: ProductsDataSource) : ProductsViewModel(repository) {
    abstract fun filterKeyword(): String
    override suspend fun query(
        coroutineContext: CoroutineContext,
        start: Int
    ) =
        repository.filterProducts(bgContext, start, true, filterKeyword())

    override suspend fun delete(coroutineContext: CoroutineContext) =
        repository.deleteAll(coroutineContext, filterKeyword())
}

class MenViewModel(repository: ProductsDataSource) : FilterViewModel(repository) {
    override fun filterKeyword() = "mens-clothes"
}

class WomenViewModel(repository: ProductsDataSource) : FilterViewModel(repository) {
    override fun filterKeyword() = "womens-clothes"
}

class AllGendersViewModel(repository: ProductsDataSource) : ProductsViewModel(repository)