package com.template.mvvm.source

import com.google.gson.Gson
import com.template.mvvm.RepositoryInjection
import com.template.mvvm.RepositoryModule
import com.template.mvvm.RepositoryTestRule
import com.template.mvvm.context
import com.template.mvvm.domain.products.Product
import com.template.mvvm.feeds.products.ProductsData
import com.template.mvvm.source.ext.read
import com.template.mvvm.source.local.DB
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers.*
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

    private lateinit var testJob: Job

    @Before
    fun setUp() {
        RepositoryModule(context())
        testJob = Job()
    }

    @After
    fun tearDown() {
        DB.INSTANCE.close()
    }

    @Test
    fun testGetAllLibraries() {
        runBlocking(testJob) {
            with(RepositoryInjection.getInstance().provideRepository(context())) {
                getAllLibraries(testJob).receiveOrNull()?.let { listOfLibs ->
                    assertThat(listOfLibs.isNotEmpty(), `is`(true))
                    assertThat(listOfLibs.size, `is`(5))
                }
            }
        }
    }

    @Test
    fun testGetAllProducts() {
        runBlocking(testJob) {
            with(RepositoryInjection.getInstance().provideRepository(context())) {
                testHelperGetProducts(
                        getAllProducts(testJob, 0),
                        "feeds/products/all.json"
                )
            }
        }
    }

    @Test
    fun testGetMenProducts() {
        runBlocking(testJob) {
            with(RepositoryInjection.getInstance().provideRepository(context())) {
                testHelperGetProducts(
                        filterProducts(testJob, 0, true, "men"),
                        "feeds/products/men.json"
                )
            }
        }
    }

    @Test
    fun testGetWomenProducts() {
        runBlocking(testJob) {
            with(RepositoryInjection.getInstance().provideRepository(context())) {
                testHelperGetProducts(
                        filterProducts(testJob, 0, true, "women"),
                        "feeds/products/women.json"
                )
            }
        }
    }

    /**
     * Helper to make request and get response from mock-backend and read mock-feeds.
     * Compare results of backend and mock-feeds.
     *
     * @param  receiveChannel The backend source.
     * @param mockFeedsFileLocation The location of file that provides feeds.
     */
    private suspend fun testHelperGetProducts(receiveChannel: ReceiveChannel<List<Product>?>, mockFeedsFileLocation: String) {
        receiveChannel.receiveOrNull()?.let { result ->
            assertThat(result.isNotEmpty(), `is`(true))
            assertThat(result.size, `is`(10))

            context().assets.read(mockFeedsFileLocation).run {
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
    }

    @Test
    fun testGetProductDetailByProduct() {
        runBlocking(testJob) {
            with(RepositoryInjection.getInstance()) {
                provideRepository(context()).run {
                    getAllProducts(testJob, 0).receiveOrNull()
                }.also { products ->
                    var failed = false
                    products?.forEach { oneProduct ->
                        provideRepository(context()).run {
                            getProductDetail(testJob, oneProduct.pid, true)
                        }.also {
                            val found = (it.receiveOrNull()?.run {
                                pid == oneProduct.pid
                            }) ?: false
                            if (!found) {
                                failed = true
                                return@forEach
                            }
                        }
                    }
                    assertThat(failed, `is`(false))
                }
            }
        }
    }

    @Test
    fun testImagesInsert() {
        runBlocking(testJob) {
            with(RepositoryInjection.getInstance()) {
                provideRepository(context()).run {
                    getAllProducts(testJob, 0).receiveOrNull()
                }.also { products ->
                    products?.forEach { oneProduct ->
                        val imagesOfItem = provideRepository(context()).run {
                            getImages(testJob, oneProduct.pid).receiveOrNull()
                        }
                        assertThat(imagesOfItem, `is`(notNullValue()))
                        assertThat(imagesOfItem?.isNotEmpty(), `equalTo`(true))

                        var failed = false

                        oneProduct.pictures.forEach { _, image ->
                            val found = imagesOfItem?.find { expImage ->
                                expImage.pid == image.pid
                                        && expImage.size == image.size
                                        && expImage.uri.toString() == image.uri.toString()
                            }
                            if (found == null) {
                                failed = true
                                return@forEach
                            }
                        }

                        assertThat(failed, `is`(false))
                    }
                }
            }
        }
    }
}