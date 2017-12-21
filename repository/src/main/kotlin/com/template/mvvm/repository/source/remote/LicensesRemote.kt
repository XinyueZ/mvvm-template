package com.template.mvvm.repository.source.remote

import com.template.mvvm.repository.LL
import com.template.mvvm.repository.contract.LicensesDataSource
import com.template.mvvm.repository.domain.licenses.Library
import kotlinx.coroutines.experimental.channels.produce
import kotlin.coroutines.experimental.CoroutineContext

class LicensesRemote : LicensesDataSource {
    override suspend fun getAllLibraries(coroutineContext: CoroutineContext, localOnly: Boolean) = produce(coroutineContext) {
        LicensesApi.service.getLibraries().execute().takeIf {
            it.isSuccessful
        }?.let {
            it.body()?.let {
                val v: List<Library> = (mutableListOf<Library>()).apply {
                    it.licenses.forEach({ licenseData ->
                        licenseData.libraries.forEach({ libraryData ->
                            add(Library.from(libraryData, licenseData))
                        })
                    })
                }
                LL.d("licenses loaded from net")
                send(v)
            } ?: kotlin.run { send(null) }
        } ?: kotlin.run { send(null) }
    }
}