package com.template.mvvm.source

import android.text.TextUtils
import com.template.mvvm.RepositoryInjection
import com.template.mvvm.RepositoryModule
import com.template.mvvm.RepositoryTestRule
import com.template.mvvm.context
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.licenses.Library
import com.template.mvvm.getValueOf
import com.template.mvvm.source.ext.read
import com.template.mvvm.source.local.DB
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
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
class TestRepository {
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
    fun testSingleton() {
        val ins_1 = RepositoryInjection.getInstance()
        val ins_2 = RepositoryInjection.getInstance()
        assertThat(ins_1, `equalTo`(ins_2))
        assertThat(ins_1.hashCode(), `is`(ins_2.hashCode()))
    }

    @Test
    fun testRepositoryReady() {
        RepositoryInjection.getInstance().provideRepository(context()).run {
            val productsSource: ProductsDataSource? = getValueOf("productsRepository")
            val licenseSource: LicensesDataSource? = getValueOf("licensesRepository")

            assertThat(productsSource, `is`(notNullValue()))
            assertThat(licenseSource, `is`(notNullValue()))
        }
    }

    @Test
    fun testGetAllLibraries() {
        runBlocking(testJob) {
            RepositoryInjection.getInstance().provideRepository(context()).run {
                launch(testJob) {
                    getAllLibraries(testJob).receiveOrNull()?.let { listOfLibs ->
                        assertThat(listOfLibs.isNotEmpty(), `is`(true))
                    }
                }.also { it.join() }
            }
        }
    }

    @Test
    fun testGetLicenses() {
        runBlocking(testJob) {
            RepositoryInjection.getInstance().provideRepository(context()).run {
                launch(testJob) {
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
                }.also { it.join() }
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
            RepositoryInjection.getInstance().provideRepository(context()).run {
                launch(testJob) {
                    getAllProducts(testJob, 0).receiveOrNull()?.let {
                        assertThat(it.isNotEmpty(), `is`(true))
                        assertThat(it.size, `is`(10))
                    }
                }.also { it.join() }
            }
        }
    }
}