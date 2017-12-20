package com.template.mvvm.models.home

import com.template.mvvm.R
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestHomeViewModel {
    private lateinit var homeVm: HomeViewModel

    @Before
    fun setup() {
        homeVm = HomeViewModel()
    }

    @Test
    fun testHomeModelInit() {
        // Test state init
        assertThat(homeVm.state.title.get(), `equalTo`(R.string.home_title))
    }
}