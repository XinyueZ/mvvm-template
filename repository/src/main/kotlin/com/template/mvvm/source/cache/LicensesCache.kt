package com.template.mvvm.source.cache

import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import io.reactivex.Single

class LicensesCache : LicensesDataSource {

    override fun getAllLibraries(localOnly: Boolean): Single<List<Library>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}