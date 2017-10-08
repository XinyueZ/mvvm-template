package com.template.mvvm.source

import android.app.Application
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.domain.licenses.Library
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ProducerJob
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce

class LicensesRepository(app: Application,
                         private val remote: LicensesDataSource,
                         private val local: LicensesDataSource,
                         private val cache: LicensesDataSource
) : LicensesDataSource {

    override suspend fun getAllLibraries(job: Job, localOnly: Boolean) = produce(job) {
        val remoteSaveLoadLocal: (suspend () -> Unit) = {
            remote.getAllLibraries(job, localOnly).receiveOrNull()?.let {
                local.saveLibraries(job, it).consumeEach {
                    local.getAllLibraries(job, localOnly).receiveOrNull()?.let {
                        send(it)
                    }
                }
            }
        }

        if (localOnly) {
            local.getAllLibraries(job, localOnly).receiveOrNull()?.let {
                it.takeIf { it.isNotEmpty() }?.let {
                    send(it)
                } ?: run {
                    remoteSaveLoadLocal()
                }
            }
        } else {
            remoteSaveLoadLocal()
        }
    }

    override suspend fun saveLibraries(job: Job, source: List<Library>): ProducerJob<Byte> = local.saveLibraries(job, source)

    override suspend fun getLicense(app: Application, job: Job, library: Library, localOnly: Boolean) = local.getLicense(app, job, library)
}