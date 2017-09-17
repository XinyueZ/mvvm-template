package com.template.mvvm.data.repository

import android.arch.lifecycle.LifecycleRegistryOwner
import com.template.mvvm.data.domain.licenses.LibraryList
import io.reactivex.Single

interface LicensesDataSource {
    fun getAllLibraries(lifecycleOwner: LifecycleRegistryOwner): Single<LibraryList>
}