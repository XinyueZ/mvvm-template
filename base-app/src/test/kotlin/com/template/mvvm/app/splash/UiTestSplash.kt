package com.template.mvvm.app.splash

import com.template.mvvm.app.AppTestRule
import com.template.mvvm.app.finish
import com.template.mvvm.app.home.HomeActivity
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.shadows.ShadowLooper.idleMainLooper
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

// TODO Re-do this tests after google fixed data-binding.
@Ignore
@RunWith(RobolectricTestRunner::class)
class DeprecatedUiTestSplash {
    private lateinit var activityCtrl: ActivityController<SplashActivity>
    private val activity: SplashActivity
        get() = activityCtrl.get()

    @Rule
    fun test() = AppTestRule()

    @Before
    fun init() {
        activityCtrl = Robolectric.buildActivity(SplashActivity::class.java).setup()
    }

    @After
    fun tearDown() {
        activityCtrl.finish()
    }

    @Test
    fun testSplashComplete() {
        runBlocking {
            Shadows.shadowOf(activity).run {
                measureTimeMillis {
                    idleMainLooper(2000, TimeUnit.MILLISECONDS)
                }.apply {
                    assertThat(nextStartedActivity.component.className, `is`(HomeActivity::class.java.name))
                }
            }
        }
    }
}