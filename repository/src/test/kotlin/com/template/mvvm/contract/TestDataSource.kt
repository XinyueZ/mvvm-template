package com.template.mvvm.contract

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test

class TestDataSource {
    private lateinit var datasource: DataSource

    private lateinit var testJob: Job

    @Before
    fun setUp() {
        datasource = object : DataSource {}
        testJob = Job()
    }

//    suspend fun <T, E : DataSource> E.select(
//            coroutineContext: CoroutineContext, // Disposable control
//            remote: suspend () -> T?, // Fetch remote-data
//            saveAfterRemote: suspend (T) -> Unit?,  // Save data in DB after fetch remote-data
//            local: suspend () -> T?, // Fetch data from DB after getting remote-data or some error while calling remotely i.e Null returned
//            predicateAcceptLocalOnly: suspend (T) -> Boolean,// Predication for local-only, if true, the local-only works to load data from DB, otherwise try remote and save DB and fetch from DB
//            emptyT: suspend () -> T,// Last chance when local provides nothing
//            localOnly: Boolean,
//            restRemoteDataHandlers: (suspend (T) -> Unit) = {} // Rest tasks after getting remote data
//    )

    @Test
    fun testSelectLocalOnly() {
        val remoteSource = "remote source"
        val localSoruce = "local source"

        val emptyT: (suspend () -> String) = { "" }
        val predicateAcceptLocalOnly: (suspend (String) -> Boolean) = { it.isBlank() }
        val remote: (suspend () -> String?) = { remoteSource }
        val local: (suspend () -> String?) = { localSoruce }
        val saveAfterRemote: ( suspend (String) -> Unit?) = {}

        runBlocking(testJob) {
            datasource.select(
                    testJob,
                    remote,
                    saveAfterRemote,
                    local,
                    predicateAcceptLocalOnly,
                    emptyT,
                    true,
                    {}
                    )
        }
    }
}