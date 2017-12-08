package com.template.mvvm.contract

import kotlinx.coroutines.experimental.channels.produce
import kotlin.coroutines.experimental.CoroutineContext

interface DataSource {
    fun clear() = Unit
}

suspend fun <T, E : DataSource> E.select(
        coroutineContext: CoroutineContext, // Disposable control
        remote: suspend () -> T?, // Fetch remote-data
        saveAfterRemote: suspend (T) -> Unit?,  // Save data in DB after fetch remote-data
        local: suspend () -> T?, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
        predicateAcceptLocalOnly: suspend (T) -> Boolean,// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
        emptyT: suspend () -> T,// Last chance when local provides nothing
        localOnly: Boolean,
        restRemoteDataHandlers: (suspend (T) -> Unit) = {} // Rest tasks after getting remote data
) = produce(coroutineContext) {
    val fetchFromLocal: suspend (suspend (T) -> Unit, suspend () -> Unit) -> Unit = { success, fallbackIfEmpty ->
        local()?.let {
            when (predicateAcceptLocalOnly(it)) {
                true -> success(it)
                else -> fallbackIfEmpty()
            }
        } ?: kotlin.run { fallbackIfEmpty() }
    }
    val remoteCallThenWrite: (suspend () -> Unit) = {
        remote()?.let { remoteData ->
            saveAfterRemote(remoteData)
            fetchFromLocal({ send(it) }, { send(remoteData) })
            restRemoteDataHandlers(remoteData)
        } ?: kotlin.run {
            fetchFromLocal({ send(it) }, { send(emptyT()) })
        }
    }

    when (localOnly) {
        true ->
            fetchFromLocal({
                send(it)
            }, {
                remoteCallThenWrite()
            })

        else -> remoteCallThenWrite()
    }
}