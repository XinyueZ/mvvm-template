package com.template.mvvm.data.source

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.LibraryList
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class LicensesRepository(app: Application,
                         private val remote: LicensesDataSource,
                         private val local: LicensesDataSource,
                         private val cache: LicensesDataSource
) : LicensesDataSource {
    private var libraryList: LibraryList? = null

    override fun getAllLibraries(lifecycleOwner: LifecycleOwner): Single<LibraryList> {
        libraryList = libraryList ?: LibraryList()
        val ret: Single<LibraryList> = Single.zip(Single.just(libraryList), remote.getAllLibraries(lifecycleOwner), BiFunction({ p1, p2 ->
            p2.observe(lifecycleOwner, Observer {
                p1.value = it
            })
            p1
        }))
        ret.doFinally({
            clear()
        })
        return ret
    }

    override fun clear() {
        libraryList = null
    }
}