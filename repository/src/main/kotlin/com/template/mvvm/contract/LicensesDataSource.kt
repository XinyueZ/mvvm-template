package com.template.mvvm.contract

import android.content.Context
import com.template.mvvm.domain.licenses.Library
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

interface LicensesDataSource : DataSource {
    suspend fun getAllLibraries(job: Job, localOnly: Boolean = true) = produce<List<Library>?>(job) {}
    suspend fun saveLibraries(job: Job, source: List<Library>) = produce<Unit>(job) {}
    suspend fun getLicense(context: Context, job: Job, library: Library, localOnly: Boolean = true) = produce<String>(job) {}
}