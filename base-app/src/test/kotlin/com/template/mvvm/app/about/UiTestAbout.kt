package com.template.mvvm.app.about

import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.template.mvvm.app.AppTestRule
import com.template.mvvm.app.BuildConfig
import com.template.mvvm.app.R
import com.template.mvvm.app.applyView
import com.template.mvvm.app.context
import com.template.mvvm.app.finish
import com.template.mvvm.core.R.string
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
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
class DeprecatedUiTestAbout {
    private lateinit var activityCtrl: ActivityController<AboutActivity>
    private val activity: AboutActivity
        get() = activityCtrl.get()

    @Rule
    fun test() = AppTestRule()

    @Before
    fun init() {
        activityCtrl = Robolectric.buildActivity(AboutActivity::class.java).setup()
    }

    @After
    fun tearDown() {
        activityCtrl.finish()
    }

    @Test
    fun testAboutContent() {
        context().run {
            activity.applyView<Toolbar>(R.id.toolbar) {
                assertThat(getString(string.about_title), `equalTo`(title))
            }

            activity.applyView<TextView>(R.id.versionTitle) {
                assertThat(getString(string.about_version_title), `equalTo`(text))
            }

            activity.applyView<TextView>(R.id.versionContent) {
                assertThat(getString(string.about_version_content, BuildConfig.VERSION_NAME), `equalTo`(text))

            }

            activity.applyView<TextView>(R.id.descriptionTitle) {
                assertThat(getString(string.about_description_title), `equalTo`(text))
            }

            activity.applyView<TextView>(R.id.descriptionContent) {
                assertThat(getString(string.about_description_content), `equalTo`(text))

            }
        }
    }
}