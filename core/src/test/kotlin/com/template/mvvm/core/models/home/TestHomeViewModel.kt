package com.template.mvvm.core.models.home

import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.template.mvvm.core.R
import com.template.mvvm.core.arch.registerLifecycleOwner
import com.template.mvvm.core.sleepWhile
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestHomeViewModel {
    private lateinit var vm: HomeViewModel

    @Before
    fun setup() {
        vm = HomeViewModel()
    }

    @Test
    fun testHomeModelInit() {
        // Test state init
        assertThat(vm.state.title.get(), `equalTo`(R.string.home_title))
    }

    @Test
    fun testHomeCommands() {
        with(vm.controller) {
            pendingForCommand(drawerToggle, R.id.action_app_bar_indicator)
            pendingForCommand(drawerToggle, R.id.action_products)
            pendingForCommand(openProduct, R.id.action_products)
            pendingForCommand(drawerToggle, R.id.action_internet)
            pendingForCommand(openInternet, R.id.action_internet)
            pendingForCommand(drawerToggle, R.id.action_software_licenses)
            pendingForCommand(openLicenses, R.id.action_software_licenses)
            pendingForCommand(drawerToggle, R.id.action_about)
            pendingForCommand(openAbout, R.id.action_about)
            pendingForCommand(openItem2, R.id.action_men)
            pendingForCommand(openItem3, R.id.action_women)
            pendingForCommand(openItem4, R.id.action_all_genders)
            pendingForCommand(openItem5, R.id.action_categories_products)
        }
    }

    private
    fun <T> pendingForCommand(liveData: LiveData<T>, @IdRes actionId: Int) {
        var done = false
        val update = Observer<T> {
            done = true
        }
        liveData.observeForever(update)

        vm.onCommand(actionId)

        sleepWhile {
            !done
        }
        assertThat(done, `is`(true))

        liveData.removeObserver(update)
    }

    @Test
    fun testHomeAtLifecycleBegin() {
        val lifeOwner = Mockito.mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(lifeOwner)
        Mockito.`when`(lifeOwner.lifecycle).thenReturn(lifecycle)
        vm.registerLifecycleOwner(lifeOwner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        sleepWhile {
            vm.state.selectItem.get() != R.id.action_categories_products
        }
        assertThat(vm.state.selectItem.get(), `is`(R.id.action_categories_products))
    }
}