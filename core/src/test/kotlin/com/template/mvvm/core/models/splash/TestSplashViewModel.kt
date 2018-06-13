package com.template.mvvm.core.models.splash

import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.LifecycleRegistry
import androidx.fragment.app.FragmentActivity
import com.template.mvvm.core.arch.registerLifecycleOwner
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.shadows.ShadowLooper.idleMainLooper
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.system.measureTimeMillis

@RunWith(RobolectricTestRunner::class)
class TestSplashViewModel {
    private lateinit var activityCtrl: ActivityController<androidx.fragment.app.FragmentActivity>
    private val activity: androidx.fragment.app.FragmentActivity
        get() = activityCtrl.get()
    private lateinit var splashMv: SplashViewModel

    @Before
    fun setup() {
        splashMv = SplashViewModel()
        activityCtrl = Robolectric.buildActivity(androidx.fragment.app.FragmentActivity::class.java).setup()
    }

    @After
    fun tearDown() {
        activityCtrl.get().finish()
    }

    @Test
    fun testSplashComplete() {
        val owner = activity
        val lifecycle = LifecycleRegistry(owner)

        with(splashMv) {
            splashMv.registerLifecycleOwner(owner)

            var done = false
            startHome.observe({ lifecycle }) {
                done = true
            }

            lifecycle.handleLifecycleEvent(ON_START)
            measureTimeMillis {
                idleMainLooper(2000, MILLISECONDS)
                !done
            }.apply {
                assertThat(startHome.value, `is`(true))
                assertThat(done, `is`(true))
            }
        }
    }
}