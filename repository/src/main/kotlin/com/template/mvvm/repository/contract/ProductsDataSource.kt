package com.template.mvvm.repository.contract

import com.template.mvvm.repository.domain.products.Image
import com.template.mvvm.repository.domain.products.Product
import com.template.mvvm.repository.domain.products.ProductDetail
import kotlinx.coroutines.experimental.channels.produce
import kotlin.coroutines.experimental.CoroutineContext

const val INVALID_PID = Long.MIN_VALUE

interface ProductsDataSource : DataSource {
    suspend fun getProductDetail(coroutineContext: CoroutineContext, pid: Long, localOnly: Boolean = true) = produce<ProductDetail?>(coroutineContext) {}
    suspend fun getAllProducts(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean = true) = produce<List<Product>?>(coroutineContext) {}
    suspend fun filterProducts(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean = true, keyword: String) = produce<List<Product>?>(coroutineContext) {}
    suspend fun saveProducts(coroutineContext: CoroutineContext, source: List<Product>) = produce<Unit>(coroutineContext) {}
    suspend fun savePictures(coroutineContext: CoroutineContext, source: List<Product>) = produce<Unit>(coroutineContext) {}
    suspend fun saveBrand(coroutineContext: CoroutineContext, source: List<Product>) = produce<Unit>(coroutineContext) {}
    suspend fun deleteAll(coroutineContext: CoroutineContext) = produce<Unit>(coroutineContext) {}
    suspend fun deleteAll(coroutineContext: CoroutineContext, keyword: String) = produce<Unit>(coroutineContext) {}
    suspend fun getImages(coroutineContext: CoroutineContext, pid: Long = INVALID_PID) = produce<List<Image>>(coroutineContext) {}
}