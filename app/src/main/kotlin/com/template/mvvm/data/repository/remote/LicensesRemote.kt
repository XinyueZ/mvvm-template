package com.template.mvvm.data.repository.remote

import android.arch.lifecycle.LifecycleRegistryOwner
import com.template.mvvm.data.domain.licenses.Library
import com.template.mvvm.data.domain.licenses.LibraryList
import com.template.mvvm.data.repository.LicensesDataSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class LicensesRemote : LicensesDataSource {
    private val libraryList = LibraryList()
    override fun getAllLibraries(lifecycleOwner: LifecycleRegistryOwner): Single<LibraryList> {
        return Single.just(libraryList).doFinally {
            LicensesApi.service.getLibraries().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(Consumer {
                libraryList.value = arrayListOf<Library>().apply {
                    it.licenses.forEach({ licenseData ->
                        licenseData.libraries.forEach({ libraryData ->
                            this@apply.add(Library.from(libraryData, licenseData))
                        })
                    })
                }
            })
        }
    }
}