package com.template.mvvm.source.local

import android.app.Application
import com.google.gson.Gson
import com.template.mvvm.LL
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.feeds.licenses.LicensesData
import com.template.mvvm.source.ext.read
import com.template.mvvm.source.local.entities.licenses.LibraryEntity
import com.template.mvvm.source.local.entities.licenses.LicenseEntity
import io.reactivex.Single
import java.io.InputStreamReader

class LicensesLocal(private val app: Application) : LicensesDataSource {

    companion object {
        private val LICENCES_LIST_JSON = "licenses-list.json"
        private val LICENCES_BOX = "licenses-box"
        private val COPYRIGHT_HOLDERS = "<copyright holders>"
        private val YEAR = "<year>"
        private val LICENCE_BOX_LOCATION_FORMAT = "%s/%s.txt"
    }

    private val gson = Gson()

    override fun getAllLibraries(localOnly: Boolean): Single<List<Library>> = DB.INSTANCE.licensesLibrariesDao()
            .getLibraryListCount()
            .flatMap({
                when (it[0].total == 0) {
                    true -> loadLicensesFromAsset()
                    else -> loadLicensesFromDB()
                }
            })

    private fun loadLicensesFromDB() = DB.INSTANCE.licensesLibrariesDao()
            .getLibraryList()
            .flatMap({
                val v: List<Library> = (mutableListOf<Library>()).apply {
                    it.forEach {
                        this.add(it.toLibrary())
                    }
                }
                LL.d("licenses loaded from db")
                Single.just(v)
            })

    private fun loadLicensesFromAsset() = Single.just(mutableListOf<Library>().apply {
        gson.fromJson(InputStreamReader(app.assets
                .open(LICENCES_LIST_JSON)), LicensesData::class.java).licenses.forEach({ licenseData ->
            licenseData.libraries.forEach({ libraryData ->
                this@apply.add(Library.from(libraryData, licenseData))
            })
        })
        LL.d("licenses loaded from asset")
    })

    override fun saveLibraries(source: List<Library>) = source.apply {
        DB.INSTANCE.apply {
            forEach {
                licensesLibrariesDao().insertLibrary(
                        LibraryEntity.from(it)
                )
                licensesLibrariesDao().insertLicense(
                        LicenseEntity.from(it.license)
                )
            }
        }
        LL.w("licenses write to db")
    }

    override fun getLicense(app: Application, library: Library, localOnly: Boolean) = app.assets.read(String.format(LICENCE_BOX_LOCATION_FORMAT, LICENCES_BOX, library.license.name))
            .flatMap {
                io.reactivex.Single.just(
                        it
                                .replace(YEAR, library.copyright ?: "")
                                .replace(COPYRIGHT_HOLDERS, library.owner ?: ""))
            }
}