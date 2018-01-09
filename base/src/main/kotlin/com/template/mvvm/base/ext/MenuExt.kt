package com.template.mvvm.base.ext

import android.support.annotation.IdRes
import android.view.MenuItem

fun MenuItem.findSubItem(@IdRes id: Int): MenuItem? = this.subMenu.findItem(id)