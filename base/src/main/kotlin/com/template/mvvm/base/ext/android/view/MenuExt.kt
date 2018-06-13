package com.template.mvvm.base.ext.android.view

import android.view.MenuItem
import androidx.annotation.IdRes

fun MenuItem.findSubItem(@IdRes id: Int): MenuItem? = this.subMenu.findItem(id)

