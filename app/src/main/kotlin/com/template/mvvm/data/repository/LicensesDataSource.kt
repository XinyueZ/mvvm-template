package com.template.mvvm.data.repository

import com.template.mvvm.data.domain.licenses.LibraryList
import io.reactivex.Single

interface LicensesDataSource {
    fun getAllLibraries(): Single<LibraryList>
}