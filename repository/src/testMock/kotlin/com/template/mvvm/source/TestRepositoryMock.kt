package com.template.mvvm.source

import com.google.gson.Gson
import com.template.mvvm.RepositoryInjection
import com.template.mvvm.RepositoryModule
import com.template.mvvm.RepositoryTestRule
import com.template.mvvm.context
import com.template.mvvm.feeds.products.ProductsData
import com.template.mvvm.source.ext.read
import com.template.mvvm.source.local.DB
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
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

    @After
    fun tearDown() {
        DB.INSTANCE.close()
    }

    @Test
    fun testGetAllLibraries() {
        runBlocking(testJob) {
            RepositoryInjection.getInstance().provideRepository(context()).run {
                launch(testJob) {
                    getAllLibraries(testJob).receiveOrNull()?.let { listOfLibs ->
                        assertThat(listOfLibs.isNotEmpty(), `is`(true))
                        assertThat(listOfLibs.size, `is`(5))
                    }
                }.also { it.join() }
            }
        }
    }

    @Test
    fun testGetAllProducts() {
        runBlocking(testJob) {
            RepositoryInjection.getInstance().provideRepository(context()).run {
                launch(testJob) {
                    getAllProducts(testJob, 0).receiveOrNull()?.let { result ->
                        assertThat(result.isNotEmpty(), `is`(true))
                        assertThat(result.size, `is`(10))

                        context().assets.read("feeds/products/all.json").run {
                            Gson().fromJson(this, ProductsData::class.java)
                        }.also { fromDataSource ->
                            var failed = false
                            result.forEach { rs ->
                                val found = fromDataSource.products.find { ds ->
                                    rs.pid == ds.pid
                                }
                                if (found == null) {
                                    failed = true
                                    return@forEach
                                }
                            }
                            assertThat(failed, `is`(false))
                        }
                    }
                }.also { it.join() }
            }
        }
    }
}