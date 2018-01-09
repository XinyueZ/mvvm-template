package com.template.mvvm.app.home

import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.View
import com.template.mvvm.app.AppTestRule
import com.template.mvvm.app.R
import com.template.mvvm.app.advanceToNextPostedRunnable
import com.template.mvvm.app.applyView
import com.template.mvvm.app.finish
import com.template.mvvm.base.ext.findChildFragment
import com.template.mvvm.base.ext.findSubItem
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
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
        activityCtrl = Robolectric.buildActivity(HomeActivity::class.java).setup()
    }

    @After
    fun tearDown() {
        activityCtrl.finish()
    }

    @Test
    fun testSelectMenOnStart() {
        activity.applyView<BottomNavigationView>(R.id.bottomNavi) {
            assertThat(selectedItemId, `is`(R.id.action_men))
        }
    }

    @Test
    fun testOpenCloseNavigationDrawer1() {
        activity.applyView<Toolbar>(R.id.toolbar) {
            navigationContentDescription = "burg-button is good"
            val vs = arrayListOf<View>()
            findViewsWithText(vs, navigationContentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
            assertThat(vs[0].callOnClick(), `is`(true))
        }
        activity.applyView<DrawerLayout>(R.id.drawer) {
            advanceToNextPostedRunnable()
            computeScroll()
            assertThat(isDrawerOpen(GravityCompat.START), `is`(true))
        }
    }

    @Test
    fun testSelectionOnDifferentTabs() {
        activity.run {
            applyView<BottomNavigationView>(R.id.bottomNavi) {
                selectedItemId = R.id.action_men
                assertThat(findChildFragment(R.id.contentFrame, R.id.childContentFrame)?.javaClass.toString(), Matchers.equalTo(MenFragment::class.java.toString()))

                selectedItemId = R.id.action_women
                assertThat(findChildFragment(R.id.contentFrame, R.id.childContentFrame)?.javaClass.toString(), Matchers.equalTo(WomenFragment::class.java.toString()))

                selectedItemId = R.id.action_all_genders
                assertThat(findChildFragment(R.id.contentFrame, R.id.childContentFrame)?.javaClass.toString(), Matchers.equalTo(AllGendersFragment::class.java.toString()))
            }
        }
    }

    @Test
    fun testDrawerMenuItems() {
        activity.applyView<NavigationView>(R.id.drawerNavi) {
            menu?.let { nonNullMenu ->
                assertThat(nonNullMenu.findItem(R.id.action_products), CoreMatchers.`is`(notNullValue()))
                assertThat(nonNullMenu.findItem(R.id.action_internet), CoreMatchers.`is`(notNullValue()))
                assertThat(nonNullMenu.findItem(R.id.action_software_licenses), CoreMatchers.`is`(notNullValue()))
                assertThat(nonNullMenu.findItem(R.id.action_about), CoreMatchers.`is`(notNullValue()))
                assertThat(nonNullMenu.findItem(R.id.action_other).findSubItem(R.id.action_software_licenses), CoreMatchers.`is`(notNullValue()))
                assertThat(nonNullMenu.findItem(R.id.action_other).findSubItem(R.id.action_about), CoreMatchers.`is`(notNullValue()))
            }
        }
    }
}