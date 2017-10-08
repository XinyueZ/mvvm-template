package com.template.mvvm.contract

import android.app.Application
import com.template.mvvm.domain.licenses.Library
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

interface LicensesDataSource : DataSource {
    suspend fun getAllLibraries(job: Job, localOnly: Boolean = false) = produce<List<Library>>(job) {}
    suspend fun saveLibraries(job: Job, source: List<Library>) = produce<Byte>(job) {}
    suspend fun getLicense(app: Application, job: Job, library: Library, localOnly: Boolean = false) = produce<String>(job) {}
}