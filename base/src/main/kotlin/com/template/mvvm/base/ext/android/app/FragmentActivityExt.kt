package com.template.mvvm.base.ext.android.app

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

fun FragmentActivity.findChildFragment(@IdRes parent: Int, @IdRes child: Int): Fragment? =
    supportFragmentManager.findFragmentById(parent).childFragmentManager.findFragmentById(child)

fun FragmentActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun FragmentActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}