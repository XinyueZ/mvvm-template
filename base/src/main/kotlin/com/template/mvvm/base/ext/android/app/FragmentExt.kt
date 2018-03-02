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
package com.template.mvvm.base.ext.android.app

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.os.Bundle.EMPTY
import android.support.annotation.DimenRes
import android.support.annotation.Size
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import kotlin.reflect.KClass

fun Fragment.getDimensionPixel(@DimenRes res: Int) = resources.getDimensionPixelSize(res)
fun Fragment.getDimension(@DimenRes res: Int) = resources.getDimension(res)

@Size
fun Fragment.getScreenSize() = com.template.mvvm.base.utils.getScreenSize(context, 0)

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

fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun <E : Fragment, T : KClass<out E>> T.newInstance(
    context: Context?,
    args: Bundle? = EMPTY
) =
    context?.run {
        (Fragment.instantiate(
            context,
            java.name
        ) as E).apply { arguments = args }
    } ?: kotlin.run { throw NullPointerException("[Context] cannot be null.") }

inline fun <reified VM : ViewModel> KClass<out Fragment>.newInstanceWith(
    context: Context?,
    args: Bundle? = EMPTY
): Fragment =
    context?.run {
        Fragment.instantiate(
            context,
            java.name
        ).apply {
            arguments = args
            if (arguments == EMPTY) {
                arguments = Bundle()
            }
            arguments?.putSerializable("vm", VM::class.java)
        }
    } ?: kotlin.run { throw NullPointerException("[Context] cannot be null.") }