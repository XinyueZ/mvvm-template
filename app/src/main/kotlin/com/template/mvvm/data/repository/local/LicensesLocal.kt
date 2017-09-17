package com.template.mvvm.data.repository.local

import android.app.Application
import android.arch.lifecycle.LifecycleRegistryOwner
import com.google.gson.Gson
import com.template.mvvm.data.domain.licenses.Library
import com.template.mvvm.data.domain.licenses.LibraryList
import com.template.mvvm.data.feeds.licenses.LicensesData
import com.template.mvvm.data.repository.LicensesDataSource
import io.reactivex.Single
import java.io.InputStreamReader

private const val LICENCES_LIST_JSON = "licenses-list.json"
private const val LICENCES_BOX = "licenses-box"
private const val COPYRIGHT_HOLDERS = "<copyright holders>"
private const val YEAR = "<year>"
private const val LICENCE_BOX_LOCATION_FORMAT = "%s/%s.txt"

class LicensesLocal(private val app: Application) : LicensesDataSource {
    private val gson = Gson()
    private val libraryList = LibraryList()

    override fun getAllLibraries(lifecycleOwner: LifecycleRegistryOwner): Single<LibraryList> {
        return Single.create({ emitter ->
            with(libraryList) {
                loadLicenses()
                if (!emitter.isDisposed)
                    emitter.onSuccess(this)
            }
        })
    }

    private fun loadLicenses() {
        val licensesData = gson.fromJson(InputStreamReader(app.assets
                .open(LICENCES_LIST_JSON)), LicensesData::class.java)
        libraryList.value = arrayListOf<Library>().apply {
            licensesData.licenses.forEach({ licenseData ->
                licenseData.libraries.forEach({ libraryData ->
                    this@apply.add(Library.from(libraryData, licenseData))
                })
            })
        }
    }
}