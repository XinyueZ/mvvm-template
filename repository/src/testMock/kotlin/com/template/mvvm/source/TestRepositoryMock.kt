package com.template.mvvm.source

import com.template.mvvm.RepositoryInjection
import com.template.mvvm.RepositoryTestRule
import com.template.mvvm.context
import com.template.mvvm.contract.LicensesDataSource
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.getValueOf
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestRepository {
    @Rule
    fun test() = RepositoryTestRule()

    @Test
    fun testSingleton() {
        val ins_1 = RepositoryInjection.getInstance()
        val ins_2 = RepositoryInjection.getInstance()
        assertThat(ins_1, CoreMatchers.`equalTo`(ins_2))
    }

    @Test
    fun testRepositoryReady() {
        RepositoryInjection.getInstance().provideRepository(context()).run {
            val productsSource: ProductsDataSource? = getValueOf("productsRepository")
            val licenseSource: LicensesDataSource? = getValueOf("licensesRepository")

            assertThat(productsSource, CoreMatchers.`is`(CoreMatchers.notNullValue()))
            assertThat(licenseSource, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        }
    }

    @Test
    fun testGetLicenses() {
        RepositoryInjection.getInstance().provideRepository(context()).run {
        }
    }
}