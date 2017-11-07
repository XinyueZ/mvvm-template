package com.template.mvvm.source

import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.contract.select
import kotlinx.coroutines.experimental.Job

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {

    override suspend fun getAllProducts(job: Job, localOnly: Boolean) = select(
            job, // Disposable control
            { remote.getAllProducts(job, localOnly).receiveOrNull() }, // Fetch remote-data
            { local.saveProducts(job, it).receive() }, // Save data in DB after fetch remote-data
            { local.getAllProducts(job, localOnly).receiveOrNull() }, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() }, // Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() },// Last chance when local provides nothing
            localOnly,
            {
                // Rest tasks after getting remote data
                local.saveBrand(job, it).receiveOrNull()
                local.savePictures(job, it).receiveOrNull()
            }
    )

    override suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean) = select(
            job, // Disposable control
            { remote.filterProduct(job, keyword, localOnly).receiveOrNull() }, // Fetch remote-data
            { local.saveProducts(job, it).receive() }, // Save data in DB after fetch remote-data
            { local.filterProduct(job, keyword, localOnly).receiveOrNull() },// Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() },// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() }, // Last chance when local provides nothing
            localOnly,
            {
                // Rest tasks after getting remote data
                local.saveBrand(job, it).receiveOrNull()
                local.savePictures(job, it).receiveOrNull()
            }
    )

    override suspend fun getProductDetail(job: Job, pid: String, localOnly: Boolean) = local.getProductDetail(job, pid, localOnly)

    override suspend fun getAllBrands(job: Job, localOnly: Boolean) = select(
            job, // Disposable control
            { remote.getAllBrands(job, localOnly).receiveOrNull() }, // Fetch remote-data
            { local.saveBrands(job, it).receive() }, // Save data in DB after fetch remote-data
            { local.getAllBrands(job, localOnly).receiveOrNull() }, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() }, // Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() }, // Last chance when local provides nothing
            localOnly
    )
}
