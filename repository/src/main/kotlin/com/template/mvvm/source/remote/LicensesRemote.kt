package com.template.mvvm.source.remote

import com.template.mvvm.LL
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LicensesRemote : LicensesDataSource {

    override fun getAllLibraries(source: LibraryList) = LicensesApi.service.getLibraries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable({
                Completable.fromAction({
                    with(arrayListOf<Library>()) {
                        it.licenses.forEach({ licenseData ->
                            licenseData.libraries.forEach({ libraryData ->
                                add(Library.from(libraryData, licenseData))
                            })
                        })
                        source.value = this
                    }

                    LL.d("licenses loaded from net")
                })
            })

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}