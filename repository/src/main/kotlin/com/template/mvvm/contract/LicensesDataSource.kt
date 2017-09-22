package com.template.mvvm.contract

import android.arch.lifecycle.LifecycleOwner
import com.template.mvvm.domain.licenses.LibraryList
import io.reactivex.Single

interface LicensesDataSource : DataSource {
    fun getAllLibraries(lifecycleOwner: LifecycleOwner): Single<LibraryList>
}