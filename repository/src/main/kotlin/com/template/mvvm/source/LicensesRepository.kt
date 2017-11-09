package com.template.mvvm.source

import android.app.Application
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.select
import com.template.mvvm.domain.licenses.Library
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class LicensesRepository(app: Application,
                         private val remote: LicensesDataSource,
                         private val local: LicensesDataSource,
                         private val cache: LicensesDataSource
) : LicensesDataSource {
    private val compositeDisposable = CompositeDisposable()
    private fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun getAllLibraries(localOnly: Boolean) = select(
            { addToAutoDispose(it) }, // Disposable control
            { remote.getAllLibraries() }, // Fetch remote-data
            { local.saveLibraries(it) },  // Save data in DB after fetch remote-data
            { local.getAllLibraries() }, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() },// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() },// Last chance when local provides nothing
            localOnly
    )

    override fun saveLibraries(source: List<Library>) = local.saveLibraries(source)

    override fun getLicense(app: Application, library: Library, localOnly: Boolean): Single<String> {
        return local.getLicense(app, library)
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}