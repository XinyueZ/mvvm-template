package com.template.mvvm.base.ext

import android.support.annotation.IdRes
import android.support.design.internal.NavigationMenuItemView
import android.support.design.internal.NavigationMenuView
import android.support.design.widget.NavigationView
import android.view.MenuItem

fun MenuItem.findSubItem(@IdRes id: Int): MenuItem? = this.subMenu.findItem(id)

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
