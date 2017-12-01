package com.template.mvvm.contract

import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductDetail
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

interface ProductsDataSource : DataSource {
    suspend fun getProductDetail(job: Job, pid: Long, localOnly: Boolean = true) = produce<ProductDetail?>(job) {}
    suspend fun getAllProducts(job: Job, offset: Int, localOnly: Boolean = true) = produce<List<Product>?>(job) {}
    suspend fun filterProducts(job: Job, offset: Int, localOnly: Boolean = true, keyword: String) = produce<List<Product>?>(job) {}
    suspend fun saveProducts(job: Job, source: List<Product>) = produce<Unit>(job) {}
    suspend fun savePictures(job: Job, source: List<Product>) = produce<Unit>(job) {}
    suspend fun saveBrand(job: Job, source: List<Product>) = produce<Unit>(job) {}
}