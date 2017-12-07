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
                println("Is prod(${BuildConfig.FLAVOR}) & debug(${BuildConfig.DEBUG}), then use debug-tool.")
                assertThat(activeDebugTool, `is`(true))
            } else {
                println("Is prod(${BuildConfig.FLAVOR}) & debug(${BuildConfig.DEBUG})), then wouldn't use debug-tool.")
                assertThat(activeDebugTool, `is`(false))
            }
        }
    }

    @Test
    fun testProdReleaseShouldWithoutDebugTool() {
        RepositoryModule(context()).run {
            val activeDebugTool: Boolean? = getValueOf("activeDebugTool")
            if (!BuildConfig.DEBUG && TextUtils.equals(BuildConfig.FLAVOR, "prod")) {
                println("Is prod(${BuildConfig.FLAVOR}) & debug(${BuildConfig.DEBUG}), then wouldn't use debug-tool.")
                assertThat(activeDebugTool, `is`(false))
            } else {
                println("Is prod(${BuildConfig.FLAVOR}) & debug(${BuildConfig.DEBUG}), then use debug-tool.")
                assertThat(activeDebugTool, `is`(true))
            }
        }
    }
}