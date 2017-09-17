package com.template.mvvm.data.repository.cache

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistryOwner
import com.template.mvvm.data.domain.licenses.LibraryList
import com.template.mvvm.data.repository.LicensesDataSource
import io.reactivex.Single

class LicensesCache : LicensesDataSource {

    override fun getAllLibraries(lifecycleOwner: LifecycleOwner): Single<LibraryList> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}