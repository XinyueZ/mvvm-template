package com.template.mvvm.base.ext.android.app

import androidx.annotation.IdRes

fun androidx.fragment.app.FragmentActivity.findChildFragment(@IdRes parent: Int, @IdRes child: Int): androidx.fragment.app.Fragment? =
    supportFragmentManager.findFragmentById(parent)?.childFragmentManager?.findFragmentById(child)

fun androidx.fragment.app.FragmentActivity.addFragmentToActivity(fragment: androidx.fragment.app.Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun androidx.fragment.app.FragmentActivity.replaceFragmentInActivity(fragment: androidx.fragment.app.Fragment, frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}