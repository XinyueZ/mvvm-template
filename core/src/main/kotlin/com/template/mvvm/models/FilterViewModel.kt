package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

abstract class FilterViewModel(repository: ProductsDataSource, itemBinding: ItemBinding<ProductItemViewModel>) : ProductsViewModel(repository, itemBinding) {
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

class MenViewModel(repository: ProductsDataSource, itemBinding: ItemBinding<ProductItemViewModel>) : FilterViewModel(repository, itemBinding) {
    override fun filterKeyword() = "MALE"
}

class WomenViewModel(repository: ProductsDataSource, itemBinding: ItemBinding<ProductItemViewModel>) : FilterViewModel(repository, itemBinding) {
    override fun filterKeyword() = "FEMALE"
}

class AllGendersViewModel(repository: ProductsDataSource, itemBinding: ItemBinding<ProductItemViewModel>) : ProductsViewModel(repository, itemBinding)