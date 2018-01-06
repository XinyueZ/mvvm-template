package com.template.mvvm.app

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestAppModule {
    private var app: App? = null
    @Rule
    fun test() = AppTestRule()

    @Before
    fun setup() {
        app = context() as? App
    }

    @Test
    fun testAppExist() {
        assertThat(app, `is`(notNullValue()))
    }
}