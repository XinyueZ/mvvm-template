package com.template.mvvm.source.cache

import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.LibraryList
import io.reactivex.Completable

class LicensesCache : LicensesDataSource {

    override fun getAllLibraries(source: LibraryList): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}