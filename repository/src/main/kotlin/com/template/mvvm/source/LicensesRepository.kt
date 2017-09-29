package com.template.mvvm.source

import android.app.Application
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LicensesRepository(app: Application,
                         private val remote: LicensesDataSource,
                         private val local: LicensesDataSource,
                         private val cache: LicensesDataSource
) : LicensesDataSource {
    override fun getAllLibraries(localOnly: Boolean) = Flowable.create<List<Library>>({ emitter ->
        emitter.onNext(local.getAllLibraries().blockingFirst())
        if (localOnly) return@create
        emitter.onNext(local.saveLibraries(remote.getAllLibraries().blockingFirst()))
    }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    override fun saveLibraries(source: List<Library>) = local.saveLibraries(source)

    override fun getLicense(app: Application, library: Library, localOnly: Boolean): Single<String> {
        return local.getLicense(app, library)
    }

    override fun clear() {
        //TODO Some resource information should be freed here.
    }
}