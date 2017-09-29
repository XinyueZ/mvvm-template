/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.template.mvvm.ext

import android.app.Activity
import android.support.annotation.DimenRes
import android.support.annotation.IdRes
import android.support.annotation.Size
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

fun Fragment.getDimensionPixel(@DimenRes res: Int) = resources.getDimensionPixelSize(res)
fun Activity.getDimensionPixel(@DimenRes res: Int) = resources.getDimensionPixelSize(res)
fun Fragment.getDimension(@DimenRes res: Int) = resources.getDimension(res)
fun Activity.getDimension(@DimenRes res: Int) = resources.getDimension(res)
@Size fun Fragment.getScreenSize() = com.template.mvvm.utils.getScreenSize(context, 0)
@Size fun Activity.getScreenSize() = com.template.mvvm.utils.getScreenSize(this, 0)

fun FragmentActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

fun FragmentActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun Fragment.addFragmentToFragment(fragment: Fragment, tag: String) {
    childFragmentManager.transact {
        add(fragment, tag)
    }
}

fun Fragment.replaceFragmentToFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.transact {
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbar: Toolbar, action: ActionBar.() -> Unit) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        action()
    }
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}
