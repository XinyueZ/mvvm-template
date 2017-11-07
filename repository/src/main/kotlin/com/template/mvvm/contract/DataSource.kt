package com.template.mvvm.contract

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

interface DataSource {
    fun clear() = Unit
}

suspend fun <T> select(
        job: Job, // Disposable control
        remote: suspend () -> T?, // Fetch remote-data
        saveAfterRemote: suspend (T) -> Unit?,  // Save data in DB after fetch remote-data
        local: suspend () -> T?, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
        predicateAcceptLocalOnly: suspend (T) -> Boolean,// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
        emptyT: suspend () -> T,// Last chance when local provides nothing
        localOnly: Boolean,
        restRemoteDataHandlers: (suspend (T) -> Unit) = {} // Rest tasks after getting remote data
) = produce(job) {
    val remoteSaveLoadLocal: (suspend () -> Unit) = {
        remote()?.let { remoteData ->
            saveAfterRemote(remoteData)?.let {
                local()?.let {
                    send(it)
                } ?: kotlin.run {
                    send(emptyT())
                }
            }
            restRemoteDataHandlers(remoteData)
        } ?: kotlin.run {
            local()?.let {
                send(it)
            } ?: kotlin.run {
                send(emptyT())
            }
        }
    }
    localOnly.takeIf { localOnly }
            ?.let {
                local()?.let {
                    it.takeIf { predicateAcceptLocalOnly(it) }
                            ?.let { send(it) }
                            ?: kotlin.run { remoteSaveLoadLocal() }
                } ?: kotlin.run { remoteSaveLoadLocal() }
            } ?: kotlin.run { remoteSaveLoadLocal() }
}