package com.template.mvvm.source

import com.template.mvvm.RepositoryInjection
import com.template.mvvm.RepositoryModule
import com.template.mvvm.RepositoryTestRule
import com.template.mvvm.context
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestRepositoryMock {
    @Rule
    fun test() = RepositoryTestRule()

    private val testJob = Job()

    @Before
    fun setUp() {
        RepositoryModule(context())
    }

    @Test
    fun testGetAllLibraries() {
        RepositoryInjection.getInstance().provideRepository(context()).run {
            launch(testJob) {
                getAllLibraries(testJob).receiveOrNull()?.let { listOfLibs ->
                    MatcherAssert.assertThat(listOfLibs.isNotEmpty(), `is`(true))
                    MatcherAssert.assertThat(listOfLibs.size == 5, `is`(true))
                }
            }
        }
    }
}