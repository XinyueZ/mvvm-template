package com.template.mvvm.app

import android.graphics.drawable.ColorDrawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageButton
import com.template.mvvm.base.ext.android.graphics.drawable.bytesEqualTo
import com.template.mvvm.base.ext.android.graphics.drawable.pixelsEqualTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import kotlin.system.measureTimeMillis

fun Toolbar.uiTestAppearance(@StringRes appbarTitle: Int, @ColorRes appbarBackgroundColor: Int, @DrawableRes appbarNavigationButton: Int) {
    navigationContentDescription = "navi-button"

    val vs = arrayListOf<View>()
    findViewsWithText(vs, navigationContentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)

    assertThat(title.toString(), equalTo(context.getString(appbarTitle)))

    val bgColor = (background as ColorDrawable).color
    assertThat(bgColor, equalTo(context.getColor(appbarBackgroundColor)))

    measureTimeMillis {
        assertThat(true, `is`((vs[0] as ImageButton).drawable.bytesEqualTo(AppCompatResources.getDrawable(context, appbarNavigationButton))))
        assertThat(false, `is`((vs[0] as ImageButton).drawable.bytesEqualTo(AppCompatResources.getDrawable(context, R.drawable.ic_men))))
    }.apply {
        println("bytesEqualTo: $this")
    }

    measureTimeMillis {
        assertThat(true, `is`((vs[0] as ImageButton).drawable.pixelsEqualTo(AppCompatResources.getDrawable(context, appbarNavigationButton))))
        assertThat(false, `is`((vs[0] as ImageButton).drawable.pixelsEqualTo(AppCompatResources.getDrawable(context, R.drawable.ic_women))))
    }.apply {
        println("pixelsEqualTo: $this")
    }
}