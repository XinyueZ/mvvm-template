package com.template.mvvm.models

import com.template.mvvm.contract.ProductsDataSource

abstract class FilterViewModel(repository: ProductsDataSource) : ProductsViewModel(repository) {
    abstract fun filterKeyword(): String
    override suspend fun queryProducts(start: Int) = repository.filterProducts(vmJob, start, true, filterKeyword())
}

class MenViewModel(repository: ProductsDataSource) : FilterViewModel(repository) {
    override fun filterKeyword() = "men"
}

class WomenViewModel(repository: ProductsDataSource) : FilterViewModel(repository) {
    override fun filterKeyword() = "women"
}

class AllGendersViewModel(repository: ProductsDataSource) : ProductsViewModel(repository)