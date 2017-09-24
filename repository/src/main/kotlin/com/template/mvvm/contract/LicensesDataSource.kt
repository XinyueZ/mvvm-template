package com.template.mvvm.contract

import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import io.reactivex.Completable

interface LicensesDataSource : DataSource {
    fun getAllLibraries(source: LibraryList): Completable
    fun saveListOfLibrary(listOfLibrary: List<Library>): Completable = Completable.complete()
}