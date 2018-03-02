package com.template.mvvm.app.home

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.View
import com.template.mvvm.app.AppTestRule
import com.template.mvvm.app.R
import com.template.mvvm.app.about.AboutActivity
import com.template.mvvm.app.advanceToNextPostedRunnable
import com.template.mvvm.app.applyView
import com.template.mvvm.app.finish
import com.template.mvvm.app.licenses.SoftwareLicensesActivity
import com.template.mvvm.app.product.ProductsActivity
import com.template.mvvm.app.uiTestAppearance
import com.template.mvvm.base.ext.android.app.findChildFragment
import com.template.mvvm.base.ext.android.view.findSubItem
import com.template.mvvm.base.ext.android.widget.getMenuItemView
import com.template.mvvm.base.ext.android.widget.isDrawerTurnOn
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import com.template.mvvm.app.product.ProductsFragment

@Ignore
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
        activity.applyView<Toolbar>(R.id.toolbar)
            .uiTestAppearance(R.string.home_title, R.color.colorPrimary, R.drawable.ic_menu)
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
            findViewsWithText(
                vs,
                navigationContentDescription,
                View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION
            )
            assertThat(vs[0].callOnClick(), `is`(true))
        }
        activity.applyView<DrawerLayout>(R.id.drawer) {
            advanceToNextPostedRunnable()
            computeScroll()
            assertThat(isDrawerTurnOn(GravityCompat.START), `is`(true))
        }
    }

    @Test
    fun testSelectionOnDifferentTabs() {
        activity.run {
            applyView<BottomNavigationView>(R.id.bottomNavi) {
                selectedItemId = R.id.action_men
                assertThat(
                    findChildFragment(
                        R.id.contentFrame,
                        R.id.childContentFrame
                    )?.javaClass.toString(), equalTo(ProductsFragment::class.java.toString())
                )

                selectedItemId = R.id.action_women
                assertThat(
                    findChildFragment(
                        R.id.contentFrame,
                        R.id.childContentFrame
                    )?.javaClass.toString(), equalTo(ProductsFragment::class.java.toString())
                )

                selectedItemId = R.id.action_all_genders
                assertThat(
                    findChildFragment(
                        R.id.contentFrame,
                        R.id.childContentFrame
                    )?.javaClass.toString(), equalTo(ProductsFragment::class.java.toString())
                )
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
                assertThat(
                    findItem(R.id.action_other).findSubItem(R.id.action_software_licenses),
                    `is`(notNullValue())
                )
                assertThat(
                    findItem(R.id.action_other).findSubItem(R.id.action_about),
                    `is`(notNullValue())
                )
            }
        }
    }

    @Test
    fun testDrawerMenuItemSelect() {
        activity.apply {
            applyView<NavigationView>(R.id.drawerNavi) {
                menu.run {
                    shadowOf(this@apply).run {
                        performIdentifierAction(R.id.action_products, 0)
                        assertThat(
                            nextStartedActivity.component.className,
                            `is`(ProductsActivity::class.java.name)
                        )

                        performIdentifierAction(R.id.action_internet, 0)
                        val startedIntent = nextStartedActivity
                        assertThat(startedIntent.action, `is`(Intent.ACTION_VIEW))
                        assertThat(startedIntent.dataString, `is`(getString(R.string.internet_url)))

                        performIdentifierAction(R.id.action_software_licenses, 0)
                        assertThat(
                            nextStartedActivity.component.className,
                            `is`(SoftwareLicensesActivity::class.java.name)
                        )

                        performIdentifierAction(R.id.action_about, 0)
                        assertThat(
                            nextStartedActivity.component.className,
                            `is`(AboutActivity::class.java.name)
                        )
                    }
                }
            }
        }
    }

    @Test
    fun testDrawerMenuItemClick() {
        activity.apply {
            applyView<NavigationView>(R.id.drawerNavi) {
                menu.run {
                    shadowOf(this@apply).run {
                        getMenuItemView(R.id.action_products)?.performClick()
                        assertThat(
                            nextStartedActivity.component.className,
                            `is`(ProductsActivity::class.java.name)
                        )

                        getMenuItemView(R.id.action_internet)?.performClick()
                        val startedIntent = nextStartedActivity
                        assertThat(startedIntent.action, `is`(Intent.ACTION_VIEW))
                        assertThat(startedIntent.dataString, `is`(getString(R.string.internet_url)))

                        getMenuItemView(R.id.action_software_licenses)?.performClick()
                        assertThat(
                            nextStartedActivity.component.className,
                            `is`(SoftwareLicensesActivity::class.java.name)
                        )

                        getMenuItemView(R.id.action_about)?.performClick()
                        assertThat(
                            nextStartedActivity.component.className,
                            `is`(AboutActivity::class.java.name)
                        )
                    }
                }
            }
        }
    }

    @Test
    fun testDrawerMenuItemClickAndDrawerClosed() {
        activity.apply {
            applyView<DrawerLayout>(R.id.drawer) {
                applyView<NavigationView>(R.id.drawerNavi) {
                    menu.run {
                        shadowOf(this@apply).run {
                            getMenuItemView(R.id.action_products)?.performClick()
                            assertThat(isDrawerTurnOn(GravityCompat.START), `is`(false))
                            getMenuItemView(R.id.action_internet)?.performClick()
                            assertThat(isDrawerTurnOn(GravityCompat.START), `is`(false))
                            getMenuItemView(R.id.action_software_licenses)?.performClick()
                            assertThat(isDrawerTurnOn(GravityCompat.START), `is`(false))
                            getMenuItemView(R.id.action_about)?.performClick()
                        }
                    }
                }
            }
        }
    }
}