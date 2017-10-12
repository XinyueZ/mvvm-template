package com.template.mvvm.source.remote

import com.template.mvvm.LL
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import io.reactivex.Single

class LicensesRemote : LicensesDataSource {

    override fun getAllLibraries(localOnly: Boolean) = LicensesApi.service
            .getLibraries()
            .flatMap({
                val v: List<Library> = (mutableListOf<Library>()).apply {
                    it.licenses.forEach({ licenseData ->
                        licenseData.libraries.forEach({ libraryData ->
                            add(Library.from(libraryData, licenseData))
                        })
                    })
                }
                LL.d("licenses loaded from net")
                Single.just(v)
            })
}