package com.template.mvvm.source

import android.text.TextUtils
import com.google.gson.Gson
import com.template.mvvm.RepositoryInjection
import com.template.mvvm.RepositoryModule
import com.template.mvvm.RepositoryTestRule
import com.template.mvvm.context
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.domain.products.Product
import com.template.mvvm.feeds.products.ProductsData
import com.template.mvvm.source.ext.read
import com.template.mvvm.source.local.DB
import com.template.mvvm.source.remote.NetworkInjection
import com.template.mvvm.source.remote.setNetworkErrorPercent
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
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
    fun testGetLicenses() {
        runBlocking(testJob) {
            RepositoryInjection.getInstance().provideRepository(context()).run {
                getAllLibraries(testJob).receiveOrNull()?.let { listOfLibs ->
                    listOfLibs.forEach { lib ->
                        val expText = readLicenseText(lib)
                        assertThat(expText, `is`(notNullValue()))
                        assertThat(TextUtils.isEmpty(expText), `is`(false))
                        getLicense(context(), testJob, lib).receiveOrNull()?.also { licenseText ->
                            assertThat(TextUtils.isEmpty(licenseText), `is`(false))
                            assertThat(licenseText, `equalTo`(expText))
                        }
                    }
                }
            }
        }
    }

    private fun readLicenseText(lib: Library) =
            context().assets.read(String.format("%s/%s.txt", "licenses-box", lib.license.name))
                    .replace("<year>", lib.copyright ?: "")
                    .replace("<copyright holders>", lib.owner ?: "")

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
    fun testImagesInsert_1() {
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

    @Test
    fun testImagesInsert_2() {
        runBlocking(testJob) {
            RepositoryInjection.getInstance().provideRepository(context()).run {
                getAllProducts(testJob, 0).receiveOrNull()?.let { storedProducts ->
                    if (storedProducts.isNotEmpty()) {
                        val storedImages = getImages(testJob).receiveOrNull()
                        assertThat(storedImages?.size?.toLong() ?: 0L, Matchers.greaterThan(0L))

                        storedProducts.forEach { prd ->
                            prd.pictures.values.forEach { img1 ->
                                val found = storedImages?.find { img2 ->
                                    img1.uri.toString() == img2.uri.toString()
                                }
                                assertThat(found, `is`(notNullValue()))
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testDeleteAllWithoutKeyword() {
        runBlocking(testJob) {
            with(RepositoryInjection.getInstance()) {
                with(provideRepository(context())) {
                    getAllProducts(testJob, 0).receiveOrNull()?.let {
                        assertThat(it.isNotEmpty(), `is`(true))
                        assertThat(it.size, `equalTo`(10))
                    }

                    deleteAll(testJob).receiveOrNull()

                    with(provideLocalProductsRepository()) {
                        // Must use local repo, otherwise the api will also be called.
                        // check local data storage
                        val storedProducts = getAllProducts(testJob, 0).receiveOrNull()?.size ?: 0
                        assertThat(storedProducts, `equalTo`(0))
                    }

                    val storedImages = getImages(testJob).receiveOrNull()?.size ?: 0
                    assertThat(storedImages, `equalTo`(0))
                }
            }
        }
    }

    @Test
    fun testDeleteWithKeyword() {
        runBlocking(testJob) {
            with(RepositoryInjection.getInstance()) {
                with(provideRepository(context())) {

                    val alreadyDeleted = mutableListOf<Product>()
                    filterProducts(testJob, 0, true, "men").receiveOrNull()?.let {
                        assertThat(it.isNotEmpty(), `is`(true))
                        assertThat(it.size, `equalTo`(10))

                        alreadyDeleted.addAll(it)
                    }

                    filterProducts(testJob, 0, true, "women").receiveOrNull()?.let {
                        assertThat(it.isNotEmpty(), `is`(true))
                        assertThat(it.size, `equalTo`(10))
                    }


                    deleteAll(testJob, "men").receiveOrNull()
                    with(provideLocalProductsRepository()) {
                        // Must use local repo, otherwise the api will also be called.
                        // check local data storage
                        val storedProducts = filterProducts(testJob, 0, true, "men").receiveOrNull()?.size
                        assertThat(storedProducts, `equalTo`(0))

                        alreadyDeleted.forEach {
                            val prdImgs = it.pictures.values
                            val dbImgs = provideRepository(context()).getImages(testJob, it.pid).receive()
                            prdImgs.forEach { img1 ->
                                val found = dbImgs.find { img2 ->
                                    img1.uri.toString() == img2.uri.toString()
                                }
                                assertThat(found, `is`(nullValue()))
                            }
                        }
                    }

                    with(provideLocalProductsRepository()) {
                        // Must use local repo, otherwise the api will also be called.
                        // check local data storage
                        val storedProducts = filterProducts(testJob, 0, true, "women").receive()
                        assertThat(storedProducts.size, `equalTo`(10))
                        storedProducts.forEach {
                            val prdImgs = it.pictures.values
                            val dbImgs = provideRepository(context()).getImages(testJob, it.pid).receive()
                            prdImgs.forEach { img1 ->
                                val found = dbImgs.find { img2 ->
                                    img1.uri.toString() == img2.uri.toString()
                                }
                                assertThat(found, `is`(notNullValue()))
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testRepositoryContinues() {
        runBlocking(testJob) {
            with(RepositoryInjection.getInstance()) {
                with(provideRepository(context())) {
                    // Get some data "all"
                    getAllProducts(testJob, 0).receive()
                    // Get some data "men"
                    val menOnline = filterProducts(testJob, 0, true, "men").receive()
                    // Get some data "women"
                    val womenOnline = filterProducts(testJob, 0, true, "women").receive()

                    // Cut the network
                    NetworkInjection.behavior.setNetworkErrorPercent(100)

                    // Reveal   data "all"
                    getAllProducts(testJob, 0).receive()
                    // Reveal   data "women"
                    val menOffline = filterProducts(testJob, 0, true, "men").receive()
                    // Reveal   data "men"
                    val womenOffline = filterProducts(testJob, 0, true, "women").receive()

                    var menEq = true
                    menOnline?.forEach { online ->
                        val eq = menOffline?.find { offline ->
                            online.pid == offline.pid
                        }
                        if (eq == null) {
                            menEq = false
                            return@forEach
                        }
                    }
                    assertThat(menEq, `is`(true))

                    var womenEq = true
                    womenOnline?.forEach { online ->
                        val eq = womenOffline?.find { offline ->
                            online.pid == offline.pid
                        }
                        if (eq == null) {
                            womenEq = false
                            return@forEach
                        }
                    }
                    assertThat(womenEq, `is`(true))
                }
            }
        }
    }
}