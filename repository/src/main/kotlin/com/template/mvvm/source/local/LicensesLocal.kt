package com.template.mvvm.source.local

import android.app.Application
import com.google.gson.Gson
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.feeds.licenses.LicensesData
import io.reactivex.Completable
import java.io.InputStreamReader

private const val LICENCES_LIST_JSON = "licenses-list.json"
private const val LICENCES_BOX = "licenses-box"
private const val COPYRIGHT_HOLDERS = "<copyright holders>"
private const val YEAR = "<year>"
private const val LICENCE_BOX_LOCATION_FORMAT = "%s/%s.txt"

class LicensesLocal(private val app: Application) : LicensesDataSource {
    private val gson = Gson()

    override fun getAllLibraries(source: LibraryList) = Completable.create { sub ->
        loadLicenses(source)
        sub.onComplete()
        return@create
    }

    private fun loadLicenses(list: LibraryList) {
        val licensesData = gson.fromJson(InputStreamReader(app.assets
                .open(LICENCES_LIST_JSON)), LicensesData::class.java)
        list.value = arrayListOf<Library>().apply {
            licensesData.licenses.forEach({ licenseData ->
                licenseData.libraries.forEach({ libraryData ->
                    this@apply.add(Library.from(libraryData, licenseData))
                })
            })
        }
    }

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}