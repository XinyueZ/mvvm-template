package com.template.mvvm.base.ext.android.app

import android.R
import com.template.mvvm.base.context
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestContextExt {

    @Test
    fun testResourceChecks() {
        val invalidId = 0


        assertThat(context().hasDrawableRes(R.drawable.bottom_bar), `is`(true))
        assertThat(context().hasDrawableRes(invalidId), `is`(false))

        assertThat(context().hasStringRes(R.string.ok), `is`(true))
        assertThat(context().hasStringRes(invalidId), `is`(false))

        assertThat(context().hasColorRes(R.color.black), `is`(true))
        assertThat(context().hasColorRes(invalidId), `is`(false))

        assertThat(context().hasDimenRes(R.dimen.app_icon_size), `is`(true))
        assertThat(context().hasDimenRes(invalidId), `is`(false))

        assertThat(context().hasMenuRes(invalidId), `is`(false))
    }
}