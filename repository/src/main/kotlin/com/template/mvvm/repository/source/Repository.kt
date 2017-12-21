package com.template.mvvm.repository.source

import android.content.Context
import com.template.mvvm.repository.contract.LicensesDataSource
import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.domain.licenses.Library
import kotlin.coroutines.experimental.CoroutineContext

class Repository(private val licensesRepository: LicensesDataSource, private val productsRepository: ProductsDataSource) : LicensesDataSource, ProductsDataSource {
    // Application's libraries, licenses...
    override suspend fun getAllLibraries(coroutineContext: CoroutineContext, localOnly: Boolean) = licensesRepository.getAllLibraries(coroutineContext, localOnly)

    override suspend fun getLicense(context: Context, coroutineContext: CoroutineContext, library: Library, localOnly: Boolean) = licensesRepository.getLicense(context, coroutineContext, library, localOnly)

    // Products, brands, logo....
    override suspend fun getAllProducts(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean) = productsRepository.getAllProducts(coroutineContext, offset, localOnly)

    override suspend fun filterProducts(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean, keyword: String) = productsRepository.filterProducts(coroutineContext, offset, localOnly, keyword)

    override suspend fun getProductDetail(coroutineContext: CoroutineContext, pid: Long, localOnly: Boolean) = productsRepository.getProductDetail(coroutineContext, pid, localOnly)

    override suspend fun deleteAll(coroutineContext: CoroutineContext) = productsRepository.deleteAll(coroutineContext)

    override suspend fun deleteAll(coroutineContext: CoroutineContext, keyword: String) = productsRepository.deleteAll(coroutineContext, keyword)

    override suspend fun getImages(coroutineContext: CoroutineContext, pid: Long) = productsRepository.getImages(coroutineContext, pid)

    // Other...
    override fun clear() {
        licensesRepository.clear()
        productsRepository.clear()
    }
}