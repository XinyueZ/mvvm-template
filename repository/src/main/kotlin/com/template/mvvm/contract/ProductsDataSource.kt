package com.template.mvvm.contract

import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductDetail
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

interface ProductsDataSource : DataSource {
    suspend fun getProductDetail(job: Job, pid: String, localOnly: Boolean = true) = produce<ProductDetail?>(job) {}
    suspend fun getAllProducts(job: Job, localOnly: Boolean = true) = produce<List<Product>?>(job) {}
    suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean = true) = produce<List<Product>?>(job) {}
    suspend fun saveProducts(job: Job, source: List<Product>) = produce<Unit>(job) {}
    suspend fun savePictures(job: Job, source: List<Product>) = produce<Unit>(job) {}
    suspend fun saveBrand(job: Job, source: List<Product>) = produce<Unit>(job) {}
}