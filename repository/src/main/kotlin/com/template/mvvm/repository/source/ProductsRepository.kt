package com.template.mvvm.repository.source

import com.template.mvvm.repository.contract.ProductsDataSource
import com.template.mvvm.repository.contract.select
import kotlin.coroutines.experimental.CoroutineContext

class ProductsRepository(private val remote: ProductsDataSource,
                         private val local: ProductsDataSource,
                         private val cache: ProductsDataSource
) : ProductsDataSource {

    override suspend fun getAllProducts(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean) = select(
            coroutineContext, // Disposable control
            { remote.getAllProducts(coroutineContext, offset, localOnly).receiveOrNull() }, // Fetch remote-data
            { local.saveProducts(coroutineContext, it).receive() }, // Save data in DB after fetch remote-data
            { local.getAllProducts(coroutineContext, offset, localOnly).receiveOrNull() }, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() }, // Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() },// Last chance when local provides nothing
            localOnly,
            {
                // Rest tasks after getting remote data
                local.saveBrand(coroutineContext, it).receiveOrNull()
                local.savePictures(coroutineContext, it).receiveOrNull()
            }
    )

    override suspend fun filterProducts(coroutineContext: CoroutineContext, offset: Int, localOnly: Boolean, keyword: String) = select(
            coroutineContext, // Disposable control
            { remote.filterProducts(coroutineContext, offset, localOnly, keyword).receiveOrNull() }, // Fetch remote-data
            { local.saveProducts(coroutineContext, it).receive() }, // Save data in DB after fetch remote-data
            { local.filterProducts(coroutineContext, offset, localOnly, keyword).receiveOrNull() },// Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() },// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() }, // Last chance when local provides nothing
            localOnly,
            {
                // Rest tasks after getting remote data
                local.saveBrand(coroutineContext, it).receiveOrNull()
                local.savePictures(coroutineContext, it).receiveOrNull()
            }
    )

    override suspend fun getProductDetail(coroutineContext: CoroutineContext, pid: Long, localOnly: Boolean) = local.getProductDetail(coroutineContext, pid, localOnly)

    override suspend fun deleteAll(coroutineContext: CoroutineContext) = local.deleteAll(coroutineContext)

    override suspend fun deleteAll(coroutineContext: CoroutineContext, keyword: String) = local.deleteAll(coroutineContext, keyword)

    override suspend fun getImages(coroutineContext: CoroutineContext, pid: Long) = local.getImages(coroutineContext, pid)
}
