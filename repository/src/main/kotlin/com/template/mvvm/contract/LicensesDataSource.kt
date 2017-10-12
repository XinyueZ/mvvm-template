package com.template.mvvm.contract

import android.app.Application
import com.template.mvvm.domain.licenses.Library
import io.reactivex.Single

interface LicensesDataSource : DataSource {
    fun getAllLibraries(localOnly: Boolean = false): Single<List<Library>>
    fun saveLibraries(source: List<Library>): List<Library> = source
    fun getLicense(app: Application, library: Library, localOnly: Boolean = false): Single<String> = Single.just("empty license content")
}