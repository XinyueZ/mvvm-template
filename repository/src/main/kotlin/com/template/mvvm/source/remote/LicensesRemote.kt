package com.template.mvvm.source.remote

import com.template.mvvm.LL
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

class LicensesRemote : LicensesDataSource {
    override suspend fun getAllLibraries(job: Job, localOnly: Boolean) = produce<List<Library>>(job) {
        LicensesApi.service
                .getLibraries().execute().takeIf {
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
            }
        }
    }
}