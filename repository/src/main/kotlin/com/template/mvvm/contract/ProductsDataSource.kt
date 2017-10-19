package com.template.mvvm.contract

import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import com.template.mvvm.domain.products.ProductDetail
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

interface ProductsDataSource : DataSource {
    suspend fun getProductDetail(job: Job, pid: String, localOnly: Boolean = false) = produce<ProductDetail>(job) {}
    suspend fun getAllProducts(job: Job, localOnly: Boolean = false) = produce<List<Product>>(job) {}
    suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean = true) = produce<List<Product>>(job) {}
    suspend fun getAllBrands(job: Job, localOnly: Boolean = false) = produce<List<Brand>>(job) {}
    suspend fun saveProducts(job: Job, source: List<Product>) = produce<Byte>(job) {}
    suspend fun savePictures(job: Job, source: List<Product>) = produce<Byte>(job) {}
    suspend fun saveBrands(job: Job, source: List<Brand>) = produce<Byte>(job) {}
    suspend fun saveBrand(job: Job, source: List<Product>) = produce<Byte>(job) {}
}