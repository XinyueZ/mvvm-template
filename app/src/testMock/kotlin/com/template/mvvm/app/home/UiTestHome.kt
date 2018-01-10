package com.template.mvvm.app.home

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageButton
import com.template.mvvm.app.AppTestRule
import com.template.mvvm.app.R
import com.template.mvvm.app.about.AboutActivity
import com.template.mvvm.app.advanceToNextPostedRunnable
import com.template.mvvm.app.applyView
import com.template.mvvm.app.finish
import com.template.mvvm.app.licenses.SoftwareLicensesActivity
import com.template.mvvm.app.products.ProductsActivity
import com.template.mvvm.base.ext.bytesEqualTo
import com.template.mvvm.base.ext.findChildFragment
import com.template.mvvm.base.ext.findSubItem
import com.template.mvvm.base.ext.pixelsEqualTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import kotlin.system.measureTimeMillis

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
    fun testAppBarLook() {
        activity.run {
            applyView<Toolbar>(R.id.toolbar) {
                navigationContentDescription = "burg-button is good"
                val vs = arrayListOf<View>()
                findViewsWithText(vs, navigationContentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)

                assertThat(title.toString(), `equalTo`(getString(R.string.home_title)))

                val bgColor = (background as ColorDrawable).color
                assertThat(bgColor, `equalTo`(getColor(R.color.colorPrimary)))

                measureTimeMillis {
                    assertThat(true, `is`((vs[0] as ImageButton).drawable.bytesEqualTo(AppCompatResources.getDrawable(context, R.drawable.ic_menu))))
                    assertThat(false, `is`((vs[0] as ImageButton).drawable.bytesEqualTo(AppCompatResources.getDrawable(context, R.drawable.ic_men))))
                }.apply {
                    println("bytesEqualTo: $this")
                }

                measureTimeMillis {
                    assertThat(true, `is`((vs[0] as ImageButton).drawable.pixelsEqualTo(AppCompatResources.getDrawable(context, R.drawable.ic_menu))))
                    assertThat(false, `is`((vs[0] as ImageButton).drawable.pixelsEqualTo(AppCompatResources.getDrawable(context, R.drawable.ic_men))))
                }.apply {
                    println("pixelsEqualTo: $this")
                }
            }
        }
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
                assertThat(findChildFragment(R.id.contentFrame, R.id.childContentFrame)?.javaClass.toString(), equalTo(MenFragment::class.java.toString()))

                selectedItemId = R.id.action_women
                assertThat(findChildFragment(R.id.contentFrame, R.id.childContentFrame)?.javaClass.toString(), equalTo(WomenFragment::class.java.toString()))

                selectedItemId = R.id.action_all_genders
                assertThat(findChildFragment(R.id.contentFrame, R.id.childContentFrame)?.javaClass.toString(), equalTo(AllGendersFragment::class.java.toString()))
            }
        }
    }

    @Test
    fun testDrawerMenuItems() {
        activity.applyView<NavigationView>(R.id.drawerNavi) {
            menu.run {
                assertThat(findItem(R.id.action_products), `is`(notNullValue()))
                assertThat(findItem(R.id.action_internet), `is`(notNullValue()))
                assertThat(findItem(R.id.action_software_licenses), `is`(notNullValue()))
                assertThat(findItem(R.id.action_about), `is`(notNullValue()))
                assertThat(findItem(R.id.action_other).findSubItem(R.id.action_software_licenses), `is`(notNullValue()))
                assertThat(findItem(R.id.action_other).findSubItem(R.id.action_about), `is`(notNullValue()))
            }
        }
    }

    @Test

    fun testDrawerMenuItemSelection() {
        activity.apply {
            applyView<NavigationView>(R.id.drawerNavi) {
                menu.run {
                    shadowOf(this@apply).run {
                        performIdentifierAction(R.id.action_products, 0)
                        assertThat(nextStartedActivity.component.className, `is`(ProductsActivity::class.java.name))

                        performIdentifierAction(R.id.action_internet, 0)
                        val startedIntent = nextStartedActivity
                        assertThat(startedIntent.action, `is`(Intent.ACTION_VIEW))
                        assertThat(startedIntent.dataString, `is`(getString(R.string.internet_url)))

                        performIdentifierAction(R.id.action_software_licenses, 0)
                        assertThat(nextStartedActivity.component.className, `is`(SoftwareLicensesActivity::class.java.name))

                        performIdentifierAction(R.id.action_about, 0)
                        assertThat(nextStartedActivity.component.className, `is`(AboutActivity::class.java.name))
                    }
                }
            }
        }
    }
}