package com.template.mvvm.data.repository.remote

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.data.repository.LicensesDataSource
import com.template.mvvm.vm.domain.licenses.Library
import com.template.mvvm.vm.domain.licenses.LibraryList
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class LicensesRemote : LicensesDataSource {
    private val libraryList = LibraryList()
    override fun getAllLibraries(lifecycleOwner: LifecycleOwner): Single<LibraryList> {
        return Single.zip(Single.just(libraryList), LicensesApi.service.getLibraries().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()), BiFunction({ p1, p2 ->
            libraryList.apply {
                value = arrayListOf<Library>().apply {
                    p2.licenses.forEach({ licenseData ->
                        licenseData.libraries.forEach({ libraryData ->
                            add(Library.from(libraryData, licenseData))
                        })
                    })
                }
            }
        }))
    }
}