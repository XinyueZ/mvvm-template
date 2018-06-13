package com.template.mvvm.app.about

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import android.support.v7.widget.Toolbar
import com.template.mvvm.app.BuildConfig
import com.template.mvvm.app.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.Is
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// TODO Re-do this tests after google fixed data-binding.
@Ignore
@RunWith(AndroidJUnit4::class)
class UiTestAbout {
    private var mActivityTestRule: ActivityTestRule<AboutActivity> =  ActivityTestRule(AboutActivity::class.java)

    @Rule
    fun test() = mActivityTestRule

    @Test
    fun testAboutContent() {
        with(mActivityTestRule.activity) {
            onView(ViewMatchers.withId(R.id.toolbar)).check(
                matches(
                    withToolbarTitle(
                        Is.`is`<CharSequence>(
                            getString(R.string.about_title)
                        )
                    )
                )
            )
            onView(withId(R.id.versionTitle)).check(matches(withText(getString(R.string.about_version_title))))
            onView(withId(R.id.versionContent)).check(
                matches(
                    withText(
                        getString(
                            com.template.mvvm.core.R.string.about_version_content,
                            BuildConfig.VERSION_NAME
                        )
                    )
                )
            )
            onView(withId(R.id.descriptionTitle)).check(matches(withText(getString(R.string.about_description_title))))
            onView(withId(R.id.descriptionContent)).check(matches(withText(getString(R.string.about_description_content))))

        }
    }

    private fun withToolbarTitle(textMatcher: Matcher<CharSequence>): Matcher<Any> {
        return object : BoundedMatcher<Any, Toolbar>(Toolbar::class.java) {
            public override fun matchesSafely(toolbar: Toolbar): Boolean {
                return textMatcher.matches(toolbar.title)
            }

            override fun describeTo(description: Description) {
                description.appendText("with toolbar title: ")
                textMatcher.describeTo(description)
            }
        }
    }
}