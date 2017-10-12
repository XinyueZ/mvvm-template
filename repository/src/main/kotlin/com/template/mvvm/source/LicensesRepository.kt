package com.template.mvvm.source

import android.app.Application
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class LicensesRepository(app: Application,
                         private val remote: LicensesDataSource,
                         private val local: LicensesDataSource,
                         private val cache: LicensesDataSource
) : LicensesDataSource {
    private val compositeDisposable = CompositeDisposable()
    private fun addToAutoDispose(vararg disposables: Disposable) {
        compositeDisposable.addAll(*disposables)
    }

    override fun getAllLibraries(localOnly: Boolean) = Single.create<List<Library>>({ emitter ->
        val remoteCallAndWrite = {
            addToAutoDispose(remote.getAllLibraries().subscribe(Consumer {
                local.saveLibraries(it)
                addToAutoDispose(local.getAllLibraries().subscribe(Consumer { if (it.isNotEmpty()) emitter.onSuccess(it) }))
            }))
        }
        if (localOnly) {
            addToAutoDispose(local.getAllLibraries().subscribe(Consumer
            {
                if (it.isNotEmpty()) emitter.onSuccess(it)
                else remoteCallAndWrite()
            }
            ))
            return@create
        }
        remoteCallAndWrite()
    }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

    override fun saveLibraries(source: List<Library>) = local.saveLibraries(source)

    override fun getLicense(app: Application, library: Library, localOnly: Boolean): Single<String> {
        return local.getLicense(app, library)
    }

    override fun clear(): Boolean {
        compositeDisposable.clear()
        return true
    }
}