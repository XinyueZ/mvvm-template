package com.template.mvvm.contract

import com.template.mvvm.domain.products.Image
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductDetail
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

const val INVALID_PID = Long.MIN_VALUE

interface ProductsDataSource : DataSource {
    suspend fun getProductDetail(job: Job, pid: Long, localOnly: Boolean = true) = produce<ProductDetail?>(job) {}
    suspend fun getAllProducts(job: Job, offset: Int, localOnly: Boolean = true) = produce<List<Product>?>(job) {}
    suspend fun filterProducts(job: Job, offset: Int, localOnly: Boolean = true, keyword: String) = produce<List<Product>?>(job) {}
    suspend fun saveProducts(job: Job, source: List<Product>) = produce<Unit>(job) {}
    suspend fun savePictures(job: Job, source: List<Product>) = produce<Unit>(job) {}
    suspend fun saveBrand(job: Job, source: List<Product>) = produce<Unit>(job) {}
    suspend fun deleteAll(job: Job) = produce<Unit>(job) {}
    suspend fun deleteAll(job: Job, keyword: String) = produce<Unit>(job) {}
    suspend fun getImages(job: Job, pid: Long = INVALID_PID) = produce<List<Image>>(job) {}
}