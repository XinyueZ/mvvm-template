package com.template.mvvm.source.remote

import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class LicensesRemote : LicensesDataSource {

    override fun getAllLibraries(source: LibraryList): Completable {
        val ret: Single<LibraryList> = Single.zip(Single.just(source), LicensesApi.service?.getLibraries()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread()), BiFunction({ p1, p2 ->
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
        return ret.toCompletable()
    }

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}