package com.template.mvvm.models.about

import com.template.mvvm.R
import com.template.mvvm.R.string.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class TestAboutViewModel {
    private lateinit var aboutMv: AboutViewModel

    @Before
    fun setup() {
        aboutMv = AboutViewModel()
    }

    @Test
    fun testModelInit() {
        with(aboutMv.state) {
            assertThat(goBack.get(), `is`(false))

            assertThat(title.get(), `is`(about_title))
            assertThat(versionTitle.get(), `is`(about_version_title))
            assertThat(versionContent.get(), `is`(about_version_content))
            assertThat(descriptionTitle.get(), `is`(about_description_title))
            assertThat(descriptionContent.get(), `is`(about_description_content))
        }
    }

    @Test
    fun testGoBack() {
        with(aboutMv) {
            onCommand(R.id.action_app_bar_indicator)
            assertThat(state.goBack.get(), `is`(true))
        }
    }
}