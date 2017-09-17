package com.template.mvvm.data.repository

import android.app.Application
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import com.template.mvvm.data.domain.licenses.LibraryList
import com.template.mvvm.data.repository.cache.LicensesCache
import com.template.mvvm.data.repository.local.LicensesLocal
import com.template.mvvm.data.repository.remote.LicensesRemote
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class LicensesRepository(app: Application,
                         private val remote: LicensesDataSource = LicensesRemote(),
                         private val local: LicensesDataSource = LicensesLocal(app),
                         private val cache: LicensesDataSource = LicensesCache()
) : LicensesDataSource {
    private val libraryList = LibraryList()

    override fun getAllLibraries(lifecycleOwner: LifecycleRegistryOwner): Single<LibraryList> {
        return Single.zip(Single.just(libraryList), remote.getAllLibraries(lifecycleOwner), BiFunction({ p1, p2 ->
            p2.observe(lifecycleOwner, Observer {
                p1.value = it
            })
            libraryList
        }))
    }
}