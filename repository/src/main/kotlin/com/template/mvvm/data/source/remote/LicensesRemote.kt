package com.template.mvvm.data.source.remote

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class LicensesRemote : LicensesDataSource {
    private var libraryList: LibraryList? = null

    override fun getAllLibraries(lifecycleOwner: LifecycleOwner): Single<LibraryList> {
        libraryList = libraryList ?: LibraryList()
        val ret: Single<LibraryList> = Single.zip(Single.just(libraryList), LicensesApi.service?.getLibraries()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread()), BiFunction({ p1, p2 ->
            p1.apply {
                value = arrayListOf<Library>().apply {
                    p2.licenses.forEach({ licenseData ->
                        licenseData.libraries.forEach({ libraryData ->
                            add(Library.from(libraryData, licenseData))
                        })
                    })
                }
            }
        }))
        ret.doFinally({
            clear()
        })
        return ret
    }

    override fun clear() {
        libraryList = null
    }
}