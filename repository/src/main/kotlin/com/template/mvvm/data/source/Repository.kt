package com.template.mvvm.data.source

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.ProductList
import io.reactivex.Single

class Repository(private val licensesRepository: LicensesRepository, private val productsRepository: ProductsRepository) : LicensesDataSource, ProductsDataSource {
    override fun getAllLibraries(lifecycleOwner: LifecycleOwner) = licensesRepository.getAllLibraries(lifecycleOwner)
    override fun getAllProducts(lifecycleOwner: LifecycleOwner): Single<ProductList> = productsRepository.getAllProducts(lifecycleOwner)
}