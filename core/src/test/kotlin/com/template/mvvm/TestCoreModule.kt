package com.template.mvvm

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestCoreModule {

    @Rule
    fun test() = CoreTestRule()

    @Before
    fun setup() {
        CoreModule(context())
    }
}