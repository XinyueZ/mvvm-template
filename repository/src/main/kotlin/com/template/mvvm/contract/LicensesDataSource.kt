package com.template.mvvm.contract

import android.app.Application
import com.template.mvvm.domain.licenses.Library
import io.reactivex.Flowable
import kotlinx.coroutines.experimental.channels.Channel

interface LicensesDataSource : DataSource {
    fun getAllLibraries(localOnly: Boolean = false): Flowable<List<Library>>
    fun saveLibraries(source: List<Library>): List<Library> = source
    fun getLicense(app: Application, library: Library, localOnly: Boolean = false) = Channel<String>()
}