package com.template.mvvm.data.repository.cache

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.data.repository.LicensesDataSource
import com.template.mvvm.vm.domain.licenses.LibraryList
import io.reactivex.Single

class LicensesCache : LicensesDataSource {

    override fun getAllLibraries(lifecycleOwner: LifecycleOwner): Single<LibraryList> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}