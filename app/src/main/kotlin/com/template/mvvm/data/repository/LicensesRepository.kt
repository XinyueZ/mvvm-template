package com.template.mvvm.data.repository

import android.app.Application
import com.template.mvvm.data.domain.licenses.LibraryList
import com.template.mvvm.data.repository.cache.LicensesCache
import com.template.mvvm.data.repository.local.LicensesLocal
import com.template.mvvm.data.repository.remote.LicensesRemote
import io.reactivex.Single

class LicensesRepository(app: Application,
                         private val remote: LicensesDataSource = LicensesRemote(),
                         private val local: LicensesDataSource = LicensesLocal(app),
                         private val cache: LicensesDataSource = LicensesCache()
) : LicensesDataSource {

    override fun getAllLibraries(): Single<LibraryList> {
        return remote.getAllLibraries()
    }
}