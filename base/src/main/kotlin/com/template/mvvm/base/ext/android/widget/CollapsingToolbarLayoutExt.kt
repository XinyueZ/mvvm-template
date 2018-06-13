package com.template.mvvm.base.ext.android.widget

import androidx.palette.graphics.Palette
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.template.mvvm.base.ext.lang.inverted

fun CollapsingToolbarLayout.setPalette(palette: Palette) {
    with(palette) {
        when (swatches.isEmpty()) {
            false -> {
                with(swatches[0]) {
                    val barColor = rgb
                    setContentScrimColor(barColor)
                    setStatusBarScrimColor(barColor)
                    rgb.inverted().let { inverted ->
                        setExpandedTitleColor(inverted)
                        setCollapsedTitleTextColor(inverted)
                    }
                }
            }
        }
    }
}