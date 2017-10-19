package com.template.mvvm.source

import com.template.mvvm.contract.ProductsDataSource
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {

    override suspend fun getAllProducts(job: Job, localOnly: Boolean) = produce(job) {
        val remoteSaveLoadLocal: (suspend () -> Unit) = {
            remote.getAllProducts(job, localOnly).receiveOrNull()?.let {
                local.saveProducts(job, it).consumeEach {
                    local.getAllProducts(job, localOnly).receiveOrNull()?.let {
                        send(it)
                    }
                }
                local.saveBrand(job, it).receiveOrNull()
                local.savePictures(job, it).receiveOrNull()
            }
        }
        if (localOnly) {
            local.getAllProducts(job, localOnly).receiveOrNull()?.let {
                it.takeIf { it.isNotEmpty() }?.let {
                    send(it)
                } ?: run {
                    remoteSaveLoadLocal()
                }
            }
        } else {
            remoteSaveLoadLocal()
        }
    }

    override suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean) = produce(job) {
        val remoteSaveLoadLocal: (suspend () -> Unit) = {
            remote.filterProduct(job, keyword, localOnly).receiveOrNull()?.let {
                local.saveProducts(job, it).consumeEach {
                    local.filterProduct(job, keyword, localOnly).receiveOrNull()?.let {
                        send(it)
                    }
                }
                local.saveBrand(job, it).receiveOrNull()
                local.savePictures(job, it).receiveOrNull()
            }
        }
        if (localOnly) {
            local.filterProduct(job, keyword, localOnly).receiveOrNull()?.let {
                it.takeIf { it.isNotEmpty() }?.let {
                    send(it)
                } ?: run {
                    remoteSaveLoadLocal()
                }
            }
        } else {
            remoteSaveLoadLocal()
        }
    }

    override suspend fun getProductDetail(job: Job, pid: String, localOnly: Boolean) = local.getProductDetail(job, pid, localOnly)

    override suspend fun getAllBrands(job: Job, localOnly: Boolean) = produce(job) {
        val remoteSaveLoadLocal: (suspend () -> Unit) = {
            remote.getAllBrands(job, localOnly).receiveOrNull()?.let {
                local.saveBrands(job, it).consumeEach {
                    local.getAllBrands(job, localOnly).receiveOrNull()?.let {
                        send(it)
                    }
                }
            }
        }
        if (localOnly) {
            local.getAllBrands(job, localOnly).receiveOrNull()?.let {
                it.takeIf { it.isNotEmpty() }?.let {
                    send(it)
                } ?: run {
                    remoteSaveLoadLocal()
                }
            }
        } else {
            remoteSaveLoadLocal()
        }
    }
}
