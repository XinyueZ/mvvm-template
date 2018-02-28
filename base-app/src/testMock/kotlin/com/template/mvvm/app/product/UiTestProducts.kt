package com.template.mvvm.app.product

import android.support.v7.widget.Toolbar
import com.template.mvvm.app.AppTestRule
import com.template.mvvm.app.R
import com.template.mvvm.app.applyView
import com.template.mvvm.app.finish
import com.template.mvvm.app.uiTestAppearance
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@Ignore
@RunWith(RobolectricTestRunner::class)
class UiTestProducts {
    private lateinit var activityCtrl: ActivityController<ProductsActivity>
    private val activity: ProductsActivity
        get() = activityCtrl.get()

    @Rule
    fun test() = AppTestRule()

    @Before
    fun init() {
        activityCtrl = Robolectric.buildActivity(ProductsActivity::class.java).setup()
    }

    @After
    fun tearDown() {
        activityCtrl.finish()
    }

    @Test
    fun testAppBarLook() {
        activity.applyView<Toolbar>(R.id.toolbar).uiTestAppearance(R.string.product_list_title, R.color.colorPrimary, R.drawable.ic_arrow_back)
    }

    @Test
    fun testLoadFirstPage() {
        // TODO test first produect page
    }

    @Test
    fun testLoadNextPages() {
        // TODO test first produect page
    }
}