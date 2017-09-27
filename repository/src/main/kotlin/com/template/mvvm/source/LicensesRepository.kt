package com.template.mvvm.source

import android.app.Application
import com.template.mvvm.LL
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.LibraryList
import io.reactivex.Completable

class LicensesRepository(app: Application,
                         private val remote: LicensesDataSource,
                         private val local: LicensesDataSource,
                         private val cache: LicensesDataSource
) : LicensesDataSource {
    override fun getAllLibraries(source: LibraryList): Completable {
        val localQuery = local.getAllLibraries(source)
        val remoteQuery = remote.getAllLibraries(source)
        val write = local.saveLibraries(source)
        return localQuery.concatWith(remoteQuery).concatWith(write).doOnDispose {
            LL.i("licenses disposed")
        }
    }

    override fun saveLibraries(source: LibraryList) = local.saveLibraries(source)
    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}