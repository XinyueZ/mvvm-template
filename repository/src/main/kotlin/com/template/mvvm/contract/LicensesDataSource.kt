package com.template.mvvm.contract

import com.template.mvvm.domain.licenses.Library
import io.reactivex.Flowable

interface LicensesDataSource : DataSource {
    fun getAllLibraries(): Flowable<List<Library>>
    fun saveLibraries(source: List<Library>): List<Library> = source
}