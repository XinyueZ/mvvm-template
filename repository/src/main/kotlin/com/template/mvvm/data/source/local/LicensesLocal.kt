package com.template.mvvm.data.source.local

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import com.google.gson.Gson
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.data.feeds.licenses.LicensesData
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import io.reactivex.Single
import java.io.InputStreamReader

private const val LICENCES_LIST_JSON = "licenses-list.json"
private const val LICENCES_BOX = "licenses-box"
private const val COPYRIGHT_HOLDERS = "<copyright holders>"
private const val YEAR = "<year>"
private const val LICENCE_BOX_LOCATION_FORMAT = "%s/%s.txt"

class LicensesLocal(private val app: Application) : LicensesDataSource {
    private val gson = Gson()
    private var libraryList: LibraryList? = null

    override fun getAllLibraries(lifecycleOwner: LifecycleOwner): Single<LibraryList> {
        val ret: Single<LibraryList> = Single.create({ emitter ->
            libraryList = (libraryList ?: LibraryList()).apply {
                loadLicenses(this)
                if (!emitter.isDisposed)
                    emitter.onSuccess(this)
            }
        })
        ret.doFinally({
            clear()
        })
        return ret
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
        libraryList = null
    }
}