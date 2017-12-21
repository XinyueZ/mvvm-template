package com.template.mvvm.core.models.splash

import android.arch.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLooper
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

@RunWith(RobolectricTestRunner::class)
class TestSplashViewModel {
    private lateinit var splashMv: SplashViewModel

    @Before
    fun setup() {
        splashMv = SplashViewModel()
    }

    @Test
    fun testSplashComplete() {
        runBlocking {
            with(splashMv) {
                ProcessLifecycleOwner.get().run {
                    var done = false
                    startHome.observeForever {
                        done = true
                    }
                    splashMv.registerLifecycleOwner(this)
                    measureTimeMillis {
                        ShadowLooper.idleMainLooper(2000, TimeUnit.MILLISECONDS)
                        !done
                    }.apply {
                        assertThat(startHome.value, `is`(true))
                        assertThat(done, `is`(true))
                    }
                }
            }
        }
    }
}