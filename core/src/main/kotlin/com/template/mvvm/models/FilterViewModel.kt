package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.ComputationToMainScheduleSingle
import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import me.tatarka.bindingcollectionadapter2.ItemBinding

abstract class FilterViewModel(repository: ProductsDataSource, itemBinding: ItemBinding<ProductItemViewModel>) : ProductsViewModel(repository, itemBinding) {
    abstract fun filterKeyword(): String

    override fun loadAllProducts(lifecycleOwner: LifecycleOwner, localOnly: Boolean) {
        productListSource?.let {
            addToAutoDispose(
                    repository.filterProduct(filterKeyword(), localOnly)
                            .compose(ComputationToMainScheduleSingle())
                            .subscribe(
                                    {
                                        productListSource?.value = it
                                        LL.i("filter ${filterKeyword()} productListSource subscribe")
                                    },
                                    {
                                        canNotLoadProducts(it, lifecycleOwner)
                                        LL.d(it.message ?: "")
                                    })
            )
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