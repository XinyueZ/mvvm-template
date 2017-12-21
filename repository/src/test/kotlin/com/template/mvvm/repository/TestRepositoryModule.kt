package com.template.mvvm.repository

import com.template.mvvm.repository.source.local.DB
import com.template.mvvm.repository.source.remote.LicensesApi
import com.template.mvvm.repository.source.remote.ProductsApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestRepositoryModule {
    @Rule
    fun test() = RepositoryTestRule()

    @Test
    fun testInitOfModule() {
        RepositoryModule(context()).run {
            assertThat(DB.INSTANCE, `is`(notNullValue()))
            assertThat(ProductsApi.service, `is`(notNullValue()))
            assertThat(LicensesApi.service, `is`(notNullValue()))
        }
    }
}