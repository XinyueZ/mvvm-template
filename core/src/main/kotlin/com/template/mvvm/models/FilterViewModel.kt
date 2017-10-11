package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch

abstract class FilterViewModel(repository: ProductsDataSource) : ProductsViewModel(repository) {
    abstract fun filterKeyword(): String

    override fun loadAllProducts(lifecycleOwner: LifecycleOwner, localOnly: Boolean) {
        productListSource?.let {
            launch(vmJob + UI + CoroutineExceptionHandler({ _, e ->
                canNotLoadProducts(e, lifecycleOwner)
                LL.d(e.message ?: "")
            })) {
                repository.filterProduct(vmJob, filterKeyword(), localOnly).consumeEach {
                    LL.i("filter ${filterKeyword()} productListSource subscribe")
                    productListSource?.value = it
                }
            }
        }
    }
}

class MenViewModel(repository: ProductsDataSource) : FilterViewModel(repository) {
    override fun filterKeyword() = "MALE"
}

class WomenViewModel(repository: ProductsDataSource) : FilterViewModel(repository) {
    override fun filterKeyword() = "FEMALE"
}

class AllGendersViewModel(repository: ProductsDataSource) : ProductsViewModel(repository)