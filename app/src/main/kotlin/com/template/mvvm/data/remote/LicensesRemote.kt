package com.template.mvvm.data.remote

import com.template.mvvm.data.LicensesDataSource
import com.template.mvvm.data.domain.licenses.LibraryList
import io.reactivex.Single

class LicensesRemote : LicensesDataSource {

    override fun getAllLibraries(): Single<LibraryList> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}