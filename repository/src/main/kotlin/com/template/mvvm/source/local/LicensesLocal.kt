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
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ProducerJob
import kotlinx.coroutines.experimental.channels.produce
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

    override suspend fun getAllLibraries(job: Job, localOnly: Boolean): ProducerJob<List<Library>> {
        DB.INSTANCE.licensesLibrariesDao()
                .getLibraryListCount().takeIf { it.isNotEmpty() }?.let {
            return when (it[0].total == 0) {
                true -> loadLicensesFromAsset(job)
                else -> loadLicensesFromDB(job)
            }
        } ?: kotlin.run {
            return produce(job) {
                send(emptyList())
            }
        }
    }

    private suspend fun loadLicensesFromDB(job: Job) = produce<List<Library>>(job) {
        val res = with(DB.INSTANCE.licensesLibrariesDao()
                .getLibraryList()) {
            mutableListOf<Library>().apply {
                this@with.forEach {
                    this.add(it.toLibrary())
                }
            }
        }
        LL.d("licenses loaded from db")
        send(res)
    }

    private suspend fun loadLicensesFromAsset(job: Job) = produce(job) {
        val licensesData = gson.fromJson(InputStreamReader(app.assets
                .open(LICENCES_LIST_JSON)), LicensesData::class.java)
        val v: List<Library> = mutableListOf<Library>().apply {
            licensesData.licenses.forEach({ licenseData ->
                licenseData.libraries.forEach({ libraryData ->
                    this@apply.add(Library.from(libraryData, licenseData))
                })
            })
        }
        LL.d("licenses loaded from asset")
        send(v)
    }

    override suspend fun saveLibraries(job: Job, source: List<Library>) = produce<List<Library>>(job) {
        DB.INSTANCE.apply {
            source.forEach {
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

    override suspend fun getLicense(app: Application, job: Job, library: Library, localOnly: Boolean) = produce(job) {
        val source = app.assets.read(String.format(LICENCE_BOX_LOCATION_FORMAT, LICENCES_BOX, library.license.name))
        send(source.replace(YEAR, library.copyright ?: "")
                .replace(COPYRIGHT_HOLDERS, library.owner ?: "")
        )
        LL.w("read licenses detail from asset")
    }
}