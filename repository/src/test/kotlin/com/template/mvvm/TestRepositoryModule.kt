package com.template.mvvm

import com.template.mvvm.source.local.DB
import com.template.mvvm.source.remote.LicensesApi
import com.template.mvvm.source.remote.ProductsApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestRepositoryModule {
    @Rule
    fun test() = RepositoryTestRule()

    @After
    fun tearDown() {
        DB.INSTANCE.close()
    }

    @Test
    fun testInitOfModule() {
        RepositoryModule(context()).run {
            assertThat(DB.INSTANCE, `is`(notNullValue()))
            assertThat(ProductsApi.service, `is`(notNullValue()))
            assertThat(LicensesApi.service, `is`(notNullValue()))
        }
    }
}