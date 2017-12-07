package com.template.mvvm

import com.template.mvvm.source.local.DB
import com.template.mvvm.source.remote.LicensesApi
import com.template.mvvm.source.remote.ProductsApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.junit.After

@RunWith(RobolectricTestRunner::class)
class TestRepositoryModuleMock {
    @Rule
    fun test() = RepositoryTestRule()

    @After
    fun tearDown() {
        DB.INSTANCE.close()
    }

    @Test
    fun testMockShouldWithoutDebugTool() {
        RepositoryModule(context()).run {
            val activeDebugTool: Boolean? = getValueOf("activeDebugTool")
            assertThat(activeDebugTool, `is`(false))
        }
    }
}