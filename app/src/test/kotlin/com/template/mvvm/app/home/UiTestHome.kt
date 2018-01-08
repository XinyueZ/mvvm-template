package com.template.mvvm.app.home

import android.support.design.widget.BottomNavigationView
import com.template.mvvm.app.AppTestRule
import com.template.mvvm.app.R
import com.template.mvvm.app.advanceToNextPostedRunnable
import com.template.mvvm.app.applyView
import com.template.mvvm.app.finish
import com.template.mvvm.app.sleepWhile
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
        activityCtrl = Robolectric.buildActivity(HomeActivity::class.java).create()
    }

    @After
    fun tearDown() {
        activityCtrl.finish()
    }

    @Test
    fun testSelectMenOnStart() {
        activity.applyView<BottomNavigationView>(R.id.bottomNavi) {
            sleepWhile {
                advanceToNextPostedRunnable()
                R.id.action_men != selectedItemId
            }
            assertThat(selectedItemId, `is`(R.id.action_men))
        }
    }
}