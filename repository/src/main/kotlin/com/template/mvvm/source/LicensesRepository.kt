package com.template.mvvm.source

import android.app.Application
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList

class LicensesRepository(app: Application,
                         private val remote: LicensesDataSource,
                         private val local: LicensesDataSource,
                         private val cache: LicensesDataSource
) : LicensesDataSource {

    override fun getAllLibraries(source: LibraryList) = remote.getAllLibraries(source)
    override fun saveListOfLibrary(listOfLibrary: List<Library>) = local.saveListOfLibrary(listOfLibrary)
    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}