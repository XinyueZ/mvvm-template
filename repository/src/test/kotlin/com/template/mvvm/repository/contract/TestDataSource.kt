package com.template.mvvm.repository.contract

import io.kotlintest.properties.Gen
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
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
    private lateinit var remoteSource: List<Int>
    private lateinit var localSource: List<Int>
    private lateinit var emptyT: (suspend () -> List<Int>)
    private lateinit var predicateAcceptLocalOnly: (suspend (List<Int>) -> Boolean)
    private lateinit var remote: (suspend () -> List<Int>?)
    private lateinit var local: (suspend () -> List<Int>?)
    private lateinit var saveAfterRemote: (suspend (List<Int>) -> Unit?)

    @Before
    fun setUp() {
        datasource = mock(DataSource::class.java)
        remoteSource = Gen.list(Gen.negativeIntegers()).generate()
        localSource = Gen.list(Gen.positiveIntegers()).generate()
        emptyT = { emptyList() }
        predicateAcceptLocalOnly = { it.isNotEmpty() }
        remote = { remoteSource }
        local = { localSource }
        saveAfterRemote = { list -> println(list.toString()) }
    }

    @Test
    fun testSelectLocalOnly() = runBlocking {
        datasource.select(
            CommonPool,
            remote,
            saveAfterRemote,
            local,
            predicateAcceptLocalOnly,
            emptyT,
            true,
            {}
        ).consumeEach {
            assertThat(
                this,
                `is`(notNullValue())
            )
            assertThat(
                it == localSource,
                `is`(true)
            )
        }
    }
}