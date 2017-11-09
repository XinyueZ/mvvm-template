package com.template.mvvm.contract

import io.reactivex.Single
import io.reactivex.disposables.Disposable

interface DataSource {
    fun clear() = Unit
}

fun <T, E : DataSource> E.select(
        autoDispose: (Disposable) -> Unit, // Disposable control
        remote: () -> Single<T>, // Fetch remote-data
        saveAfterRemote: (T) -> T,  // Save data in DB after fetch remote-data
        local: () -> Single<T>, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
        predicateAcceptLocalOnly: (T) -> Boolean,// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
        emptyT: () -> T,// Last chance when local provides nothing
        localOnly: Boolean,
        restRemoteDataHandlers: ((T) -> Unit) = {} // Rest tasks after getting remote data
): Single<T> = Single.create<T>({ emitter ->
    val fetchFromLocal: ((T) -> Unit, () -> Unit, (Throwable) -> Unit) -> Unit = { success, fallbackIfEmpty, fallbackIfDead ->
        autoDispose(
                local().subscribe(
                        {
                            when (predicateAcceptLocalOnly(it)) {
                                true -> success(it)
                                else -> fallbackIfEmpty()
                            }
                        }, { fallbackIfDead(it) }
                )
        )
    }
    val remoteCallThenWrite = {
        autoDispose(remote()
                .doAfterSuccess({ restRemoteDataHandlers(it) })
                .subscribe({ remoteData ->
                    saveAfterRemote(remoteData)
                    fetchFromLocal({ emitter.onSuccess(it) }, { emitter.onSuccess(remoteData) }, { emitter.onError(it) })
                }, {
                    fetchFromLocal({ emitter.onSuccess(it) }, { emitter.onSuccess(emptyT()) }, { emitter.onError(it) })
                }))
    }

    when (localOnly) {
        true ->
            fetchFromLocal({
                emitter.onSuccess(it)
            }, {
                remoteCallThenWrite()
            }, {
                remoteCallThenWrite()
            })

        else -> remoteCallThenWrite()
    }
})