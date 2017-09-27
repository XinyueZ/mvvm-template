package com.template.mvvm.contract

import com.template.mvvm.domain.licenses.LibraryList
import io.reactivex.Completable

interface LicensesDataSource : DataSource {
    fun getAllLibraries(source: LibraryList): Completable
    fun saveLibraries(source: LibraryList): Completable = Completable.complete()
}