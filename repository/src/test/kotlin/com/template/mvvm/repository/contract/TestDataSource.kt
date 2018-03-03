package com.template.mvvm.repository.contract

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestDataSource {
    @Mock
    private lateinit var datasource: DataSource

    private lateinit var testJob: Job

    @Before
    fun setUp() {
        datasource = mock(DataSource::class.java)
        testJob = Job()
    }

    @Test
    fun testSelectLocalOnly() {
        // TODO select() should be tested.
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