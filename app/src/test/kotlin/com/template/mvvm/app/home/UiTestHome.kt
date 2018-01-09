package com.template.mvvm.app.home

import android.support.design.widget.BottomNavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import com.template.mvvm.app.AppTestRule
import com.template.mvvm.app.R
import com.template.mvvm.app.advanceToNextPostedRunnable
import com.template.mvvm.app.applyView
import com.template.mvvm.app.finish
import com.template.mvvm.app.sleepWhile
import com.template.mvvm.core.ext.obtainViewModel
import com.template.mvvm.core.models.home.HomeViewModel
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

    /**
     *  Actually I want to run following codes which do pure UI execution to open drawer with
     *  clicking "burg button", however, there's no way to call "click" on the reflected
     *  [Toolbar.mNavButtonView].
     **/
    @Test
    fun testOpenCloseNavigationDrawer1() {
//        println("[${Thread.currentThread().name} ]")
//        activity.applyView<Toolbar>(R.id.toolbar) {
//            println("[${Thread.currentThread().name} ]")
//            val field = Toolbar::class.memberProperties.find { it.name == "mNavButtonView" }
//            field?.let { nonNullField ->
//                println("[${Thread.currentThread().name} ]")
//                nonNullField.isAccessible = true
//                val clickView = nonNullField.get(this) as ImageButton
//                println(clickView)
//                println(clickView.hashCode())
//
////                Assert.assertTrue(clickView.callOnClick())
////                Assert.assertTrue(clickView.performClick())
////                assertThat(clickView.callOnClick(), `is`(true))
//            }
//        }

//        activityCtrl.resume()

//        activity.applyView<DrawerLayout>(R.id.drawer) {
//            println("[${Thread.currentThread().name} ]")
//            assertThat(isDrawerOpen(GravityCompat.START), `is`(true))
//        }
    }

    @Test
    fun testOpenCloseNavigationDrawer2() {
        activity.applyView<DrawerLayout>(R.id.drawer) {
            openDrawer(GravityCompat.START)
            assertThat(isDrawerOpen(GravityCompat.START), `is`(true))
            closeDrawer(GravityCompat.START)
            assertThat(isDrawerOpen(GravityCompat.START), `is`(false))
        }
    }

    @Test
    fun testOpenCloseNavigationDrawer3() {
        activity.applyView<Toolbar>(R.id.toolbar) {
            activity.obtainViewModel(HomeViewModel::class.java).onCommand(R.id.action_app_bar_indicator)
        }

        activityCtrl.resume()

        activity.applyView<DrawerLayout>(R.id.drawer) {
            assertThat(isDrawerOpen(GravityCompat.START), `is`(true))
        }
    }
}