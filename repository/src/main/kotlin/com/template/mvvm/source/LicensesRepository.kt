package com.template.mvvm.source

import android.content.Context
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.select
import com.template.mvvm.domain.licenses.Library
import kotlinx.coroutines.experimental.Job

class LicensesRepository(context: Context,
                         private val remote: LicensesDataSource,
                         private val local: LicensesDataSource,
                         private val cache: LicensesDataSource
) : LicensesDataSource {

    override suspend fun getAllLibraries(job: Job, localOnly: Boolean) = select(
            job, // Disposable control
            { remote.getAllLibraries(job, localOnly).receiveOrNull() }, // Fetch remote-data
            { local.saveLibraries(job, it).receive() },  // Save data in DB after fetch remote-data
            { local.getAllLibraries(job, localOnly).receiveOrNull() }, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
            { it.isNotEmpty() },// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
            { emptyList() }, // Last chance when local provides nothing
            localOnly
    )

    override suspend fun getLicense(context: Context, job: Job, library: Library, localOnly: Boolean) = local.getLicense(context, job, library)
}