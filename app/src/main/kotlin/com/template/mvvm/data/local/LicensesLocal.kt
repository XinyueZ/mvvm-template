package com.template.mvvm.data.local

import android.app.Application
import com.google.gson.Gson
import com.template.mvvm.data.LicensesDataSource
import com.template.mvvm.data.domain.licenses.Library
import com.template.mvvm.data.domain.licenses.LibraryList
import com.template.mvvm.data.feeds.licenses.LicensesData
import io.reactivex.Single
import java.io.InputStreamReader

private const val LICENCES_LIST_JSON = "licenses-list.json"
private const val LICENCES_BOX = "licenses-box"
private const val COPYRIGHT_HOLDERS = "<copyright holders>"
private const val YEAR = "<year>"
private const val LICENCE_BOX_LOCATION_FORMAT = "%s/%s.txt"

class LicensesLocal(private val app: Application) : LicensesDataSource {
    private val gson = Gson()

    override fun getAllLibraries(): Single<LibraryList> {
        return Single.create({ emitter ->
            val licensesData = gson.fromJson(InputStreamReader(app.assets
                    .open(LICENCES_LIST_JSON)), LicensesData::class.java)
            with(LibraryList()) {
                value = arrayListOf<Library>().apply {
                    licensesData.licenses.forEach({ licenseData ->
                        licenseData.libraries.forEach({ libraryData ->
                            this@apply.add(Library.from(libraryData, licenseData))
                        })
                    })
                }
                emitter.onSuccess(this)
            }
        })
    }
}