package com.template.mvvm.repository.contract

import com.template.mvvm.repository.sleepWhile
import io.kotlintest.properties.Gen
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers.`is`
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
        saveAfterRemote = {}
        remote = { remoteSource }
        local = { localSource }
    }

    @Test
    fun shouldLocalOnlyReturnsLocalDataSource() = runBlocking {
        var shouldNotSave = true
        saveAfterRemote = {
            shouldNotSave = false
            Unit
        }

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
                it == localSource,
                `is`(true)
            )

            sleepWhile {
                !shouldNotSave
            }

            assertThat(
                shouldNotSave,
                `is`(true)
            )
        }
    }

    @Test
    fun shouldLocalOnlyNotSaveAnyData() = runBlocking {
        var shouldNotSave = true
        saveAfterRemote = {
            shouldNotSave = false
            Unit
        }

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
            sleepWhile {
                !shouldNotSave
            }

            assertThat(
                shouldNotSave,
                `is`(true)
            )
        }
    }

    @Test
    fun shouldGetRemoteSourceAndSaveToLocalFinallyGetFromLocalSource() = runBlocking {
        var save = false
        saveAfterRemote = {
            save = true
            Unit
        }

        var someRestDone = false

        datasource.select(
            CommonPool,
            remote,
            saveAfterRemote,
            local,
            predicateAcceptLocalOnly,
            emptyT,
            false,
            { someRestDone = true }
        ).consumeEach {
            sleepWhile {
                !save
            }

            assertThat(
                save,
                `is`(true)
            )
            assertThat(
                someRestDone,
                `is`(true)
            )

            assertThat(
                it == localSource,
                `is`(true)
            )
        }
    }

    @Test
    fun shouldDoRestRemoteDataHandlersAfterGettingRemoteSource() = runBlocking {
        var someRestDone = false

        datasource.select(
            CommonPool,
            remote,
            saveAfterRemote,
            local,
            predicateAcceptLocalOnly,
            emptyT,
            false,
            { someRestDone = true }
        ).consumeEach {
            sleepWhile {
                !someRestDone
            }
            assertThat(
                someRestDone,
                `is`(true)
            )
        }
    }

    @Test
    fun shouldNotSaveWhenRemoteNull() = runBlocking {
        remote = { null }
        local = { localSource }

        var shouldNotSave = true
        saveAfterRemote = {
            shouldNotSave = false
            Unit
        }

        datasource.select(
            CommonPool,
            remote,
            saveAfterRemote,
            local,
            predicateAcceptLocalOnly,
            emptyT,
            false,
            { }
        ).consumeEach {
            sleepWhile {
                !shouldNotSave
            }
            assertThat(
                shouldNotSave,
                `is`(true)
            )
        }
    }

    @Test
    fun shouldNotDoRestRemoteDataHandlersWhenRemoteNull() = runBlocking {
        remote = { null }
        local = { localSource }
        var someRestDone = false

        datasource.select(
            CommonPool,
            remote,
            saveAfterRemote,
            local,
            predicateAcceptLocalOnly,
            emptyT,
            false,
            { someRestDone = true }
        ).consumeEach {
            sleepWhile {
                someRestDone
            }
            assertThat(
                someRestDone,
                `is`(false)
            )
        }
    }
}