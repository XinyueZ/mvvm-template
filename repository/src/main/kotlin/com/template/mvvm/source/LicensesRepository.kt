package com.template.mvvm.source

import android.app.Application
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.licenses.LibraryList
import com.template.mvvm.source.remote.interceptors.MissingNetworkConnectionException
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class LicensesRepository(app: Application,
                         private val remote: LicensesDataSource,
                         private val local: LicensesDataSource,
                         private val cache: LicensesDataSource
) : LicensesDataSource {
    private val compositeDisposable = CompositeDisposable()
    override fun getAllLibraries(source: LibraryList) = remote.getAllLibraries(source)
            .doOnComplete {
                source.value?.let {
                    compositeDisposable.add(local.saveListOfLibrary(it).subscribe())
                }
            }
            .doOnDispose {
                compositeDisposable.clear()
            }.onErrorResumeNext {
                when (it) {
                    is MissingNetworkConnectionException -> local.getAllLibraries(source)
                    else -> Completable.complete()
                }
            }

    override fun saveListOfLibrary(listOfLibrary: List<Library>) = local.saveListOfLibrary(listOfLibrary)
    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}