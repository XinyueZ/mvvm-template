package com.template.mvvm.core.models.product

import com.template.mvvm.repository.contract.ProductsDataSource
import kotlin.coroutines.experimental.CoroutineContext

abstract class FilterViewModel(repository: ProductsDataSource) : ProductsViewModel(repository) {
    abstract fun filterKeyword(): String
    override suspend fun query(coroutineContext: CoroutineContext, start: Int) =
        repository.filterProducts(vmJob, start, true, filterKeyword())

    override suspend fun delete(coroutineContext: CoroutineContext) = repository.deleteAll(coroutineContext, filterKeyword())
}

class MenViewModel(repository: ProductsDataSource) : FilterViewModel(repository) {
    override fun filterKeyword() = "men"
}

class WomenViewModel(repository: ProductsDataSource) : FilterViewModel(repository) {
    override fun filterKeyword() = "women"
}

class AllGendersViewModel(repository: ProductsDataSource) : ProductsViewModel(repository)