package com.template.mvvm.source

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.contract.select
import kotlinx.coroutines.experimental.Job

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {

    override suspend fun getAllProducts(job: Job, offset: Int, localOnly: Boolean) = select(
            job, // Disposable control
            { remote.getAllProducts(job, offset, localOnly).receiveOrNull() }, // Fetch remote-data
            { local.saveProducts(job, it).receive() }, // Save data in DB after fetch remote-data
            { local.getAllProducts(job, offset, localOnly).receiveOrNull() }, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() }, // Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() },// Last chance when local provides nothing
            localOnly,
            {
                // Rest tasks after getting remote data
                local.saveBrand(job, it).receiveOrNull()
                local.savePictures(job, it).receiveOrNull()
            }
    )

    override suspend fun filterProducts(job: Job, offset: Int, localOnly: Boolean, keyword: String) = select(
            job, // Disposable control
            { remote.filterProducts(job, offset, localOnly, keyword).receiveOrNull() }, // Fetch remote-data
            { local.saveProducts(job, it).receive() }, // Save data in DB after fetch remote-data
            { local.filterProducts(job, offset, localOnly, keyword).receiveOrNull() },// Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() },// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() }, // Last chance when local provides nothing
            localOnly,
            {
                // Rest tasks after getting remote data
                local.saveBrand(job, it).receiveOrNull()
                local.savePictures(job, it).receiveOrNull()
            }
    )

    override suspend fun getProductDetail(job: Job, pid: Long, localOnly: Boolean) = local.getProductDetail(job, pid, localOnly)

    override suspend fun deleteAll(job: Job) = local.deleteAll(job)

    override suspend fun deleteAll(job: Job, keyword: String) = local.deleteAll(job, keyword)

    override suspend fun getImages(job: Job, pid: Long) = local.getImages(job, pid)
}
