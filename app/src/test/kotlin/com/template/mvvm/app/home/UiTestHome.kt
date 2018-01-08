package com.template.mvvm.app.home

import com.template.mvvm.app.AppTestRule
import com.template.mvvm.app.finish
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class UiTestHome {
    private lateinit var activityCtrl: ActivityController<HomeActivity>
    private val activity: HomeActivity
        get() = activityCtrl.get()

    @Rule
    fun test() = AppTestRule()

    @Before
    fun init() {
        activityCtrl = Robolectric.buildActivity(HomeActivity::class.java).setup()
    }

    @After
    fun tearDown() {
        activityCtrl.finish()
    }
}