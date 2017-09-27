package com.template.mvvm.source.local

import android.app.Application
import com.google.gson.Gson
import com.template.mvvm.LL
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.feeds.licenses.LicensesData
import com.template.mvvm.source.local.dao.DB
import com.template.mvvm.source.local.entities.licenses.LibraryEntity
import com.template.mvvm.source.local.entities.licenses.LicenseEntity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    override fun getAllLibraries(source: LibraryList) = DB.INSTANCE.licensesLibrariesDao().getLibraryListCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable({
                when (it[0].total == 0) {
                    true -> loadLicensesFromAsset(source)
                    false -> loadLicensesFromDB(source)
                }
            })

    private fun loadLicensesFromDB(source: LibraryList) = DB.INSTANCE.licensesLibrariesDao().getLibraryList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable({
                Completable.fromAction({
                    with(ArrayList<Library>()) {
                        it.forEach {
                            this.add(it.toLibrary())
                        }
                        source.value = this
                        LL.d("licenses loaded from db")
                    }
                })
            })

    private fun loadLicensesFromAsset(list: LibraryList) = Completable.fromAction({
        val licensesData = gson.fromJson(InputStreamReader(app.assets
                .open(LICENCES_LIST_JSON)), LicensesData::class.java)
        list.value = arrayListOf<Library>().apply {
            licensesData.licenses.forEach({ licenseData ->
                licenseData.libraries.forEach({ libraryData ->
                    this@apply.add(Library.from(libraryData, licenseData))
                })
            })
        }
        LL.d("licenses loaded from asset")
    })

    override fun saveLibraries(source: LibraryList) = Completable.fromAction({
        source.value?.forEach {
            DB.INSTANCE.apply {
                licensesLibrariesDao().insertLibrary(
                        LibraryEntity.from(it)
                )
                licensesLibrariesDao().insertLicense(
                        LicenseEntity.from(it.license)
                )
            }
        }
        LL.w("licenses write to db")
    }).subscribeOn(Schedulers.io())

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}