package com.template.mvvm.source

import com.template.mvvm.RepositoryInjection
import com.template.mvvm.RepositoryModule
import com.template.mvvm.RepositoryTestRule
import com.template.mvvm.context
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.ProductsDataSource
import kotlinx.coroutines.experimental.Job
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
class TestRepository {
    @Rule
    fun test() = RepositoryTestRule()

    private lateinit var testJob: Job

    @Before
    fun setUp() {
        RepositoryModule(context())
        testJob = Job()
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
            val productsSource: ProductsDataSource? = ReflectionHelpers.getField(this, "productsRepository")
            val licenseSource: LicensesDataSource? = ReflectionHelpers.getField(this, "licensesRepository")

            assertThat(productsSource, `is`(notNullValue()))
            assertThat(licenseSource, `is`(notNullValue()))
        }
    }
}