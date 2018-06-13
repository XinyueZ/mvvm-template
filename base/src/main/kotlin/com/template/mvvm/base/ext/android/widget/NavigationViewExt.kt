package com.template.mvvm.base.ext.android.widget

import android.app.Activity
import androidx.annotation.IdRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.template.mvvm.base.ext.android.view.setUpGoldenRatioInvertedHeight
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.channels.consumeEach

fun NavigationView.getMenuItemView(@IdRes id: Int): NavigationMenuItemView? {
    for (i in 0 until childCount) {
        with(getChildAt(i)) {
            if (this is NavigationMenuView) {
                return (0 until childCount)
                    .map { getChildAt(it) }
                    .filter { it is NavigationMenuItemView }
                    .map { it as NavigationMenuItemView }
                    .firstOrNull { it.itemData.itemId == id }
            }
        }
    }
    return null
}

fun BottomNavigationView.onNavigationItemSelected(block: suspend (Int) -> Unit) {
    val eventActor = actor<Int>(
        UI,
        capacity = Channel.CONFLATED
    ) {
        // Handling only most recently received update.
        consumeEach { block(it) }
    }
    setOnNavigationItemSelectedListener {
        eventActor.offer(it.itemId)
        true
    }
}

fun NavigationView.onNavigationItemSelected(block: suspend (Int) -> Unit) {
    val eventActor = actor<Int>(
        UI,
        capacity = Channel.CONFLATED
    ) {
        // Handling only most recently received update.
        consumeEach { block(it) }
    }
    setNavigationItemSelectedListener {
        it.isChecked = true
        eventActor.offer(it.itemId)
        true
    }
}

fun NavigationView.setHeaderHeightBetter(activity: Activity) {
    getHeaderView(0).setUpGoldenRatioInvertedHeight(activity)
}