package com.template.mvvm.core.models.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.IdRes
import com.template.mvvm.core.R
import com.template.mvvm.core.sleepWhile
import org.hamcrest.CoreMatchers.`is`
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

    @Test
    fun testHomeCommands() {
        with(homeVm.controller) {
            testCommand(drawerToggle, R.id.action_app_bar_indicator)
            testCommand(drawerToggle, R.id.action_products)
            testCommand(openProduct, R.id.action_products)
            testCommand(drawerToggle, R.id.action_internet)
            testCommand(openInternet, R.id.action_internet)
            testCommand(drawerToggle, R.id.action_software_licenses)
            testCommand(openLicenses, R.id.action_software_licenses)
            testCommand(drawerToggle, R.id.action_about)
            testCommand(openAbout, R.id.action_about)
            testCommand(openItem2, R.id.action_men)
            testCommand(openItem3, R.id.action_women)
            testCommand(openItem4, R.id.action_all_genders)
        }
    }

    private fun testCommand(liveData: LiveData<Boolean>, @IdRes actionId: Int) {
        var done = false
        val update = Observer<Boolean> {
            done = true
        }
        liveData.observeForever(update)

        homeVm.onCommand(actionId)

        sleepWhile {
            !done
        }
        assertThat(done, `is`(true))

        liveData.removeObserver(update)
    }
}