package com.template.mvvm

import android.text.TextUtils
import com.template.mvvm.source.local.DB
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestRepositoryModuleProd {
    @Rule
    fun test() = RepositoryTestRule()

    @After
    fun tearDown() {
        DB.INSTANCE.close()
    }

    @Test
    fun testProdDebugShouldWithDebugTool() {
        RepositoryModule(context()).run {
            val activeDebugTool: Boolean? = getValueOf("activeDebugTool")
            if (BuildConfig.DEBUG && TextUtils.equals(BuildConfig.FLAVOR, "prod")) {
                assertThat(activeDebugTool, `is`(true))
            }
        }
    }

    @Test
    fun testProdShouldWithoutDebugTool() {
        RepositoryModule(context()).run {
            val activeDebugTool: Boolean? = getValueOf("activeDebugTool")
            if (!BuildConfig.DEBUG && TextUtils.equals(BuildConfig.FLAVOR, "prod")) {
                assertThat(activeDebugTool, `is`(false))
            }
        }
    }
}