package com.template.mvvm.repository

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
class TestRepositoryModuleMock {
    @Rule
    fun test() = RepositoryTestRule()

    @Test
    fun testMockShouldWithoutDebugTool() {
        RepositoryModule(context()).run {
            val activeDebugTool: Boolean? = ReflectionHelpers.getField(this, "activeDebugTool")
            println("Is mock(${BuildConfig.FLAVOR}), then wouldn't use debug-tool.")
            assertThat(activeDebugTool, `is`(false))
        }
    }
}