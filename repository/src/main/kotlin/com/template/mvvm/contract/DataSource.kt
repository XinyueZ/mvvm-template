package com.template.mvvm.contract

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

interface DataSource {
    fun clear() = Unit
}

suspend fun <T> select(
        job: Job, // Disposable control
        remote: suspend (Job) -> T?, // Fetch remote-data
        saveAfterRemote: suspend (Job, T) -> Unit?,  // Save data in DB after fetch remote-data
        local: suspend (Job) -> T?, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
        predicateAcceptLocalOnly: suspend (T) -> Boolean,// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
        emptyT: suspend () -> T,// Last chance when local provides nothing
        localOnly: Boolean,
        restRemoteDataHandlers: (suspend (T) -> Unit)? = null // Rest tasks after getting remote data
) = produce(job) {
    val remoteSaveLoadLocal: (suspend () -> Unit) = {
        remote(job)?.let { remoteData ->
            saveAfterRemote(job, remoteData)?.let {
                local(job)?.let {
                    send(it)
                } ?: kotlin.run {
                    send(emptyT())
                }
            }
            restRemoteDataHandlers?.let { it(remoteData) }
        } ?: kotlin.run {
            local(job)?.let {
                send(it)
            } ?: kotlin.run {
                send(emptyT())
            }
        }
    }
    localOnly.takeIf { localOnly }
            ?.let {
                local(job)?.let {
                    it.takeIf { predicateAcceptLocalOnly(it) }
                            ?.let { send(it) }
                            ?: kotlin.run { remoteSaveLoadLocal() }
                } ?: kotlin.run { remoteSaveLoadLocal() }
            } ?: kotlin.run { remoteSaveLoadLocal() }
}