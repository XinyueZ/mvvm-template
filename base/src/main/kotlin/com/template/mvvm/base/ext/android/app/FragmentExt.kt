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

import android.content.Context
import android.os.Bundle
import android.os.Bundle.EMPTY
import androidx.annotation.DimenRes
import androidx.annotation.Size
import androidx.lifecycle.ViewModel
import com.template.mvvm.base.ui.ViewModelDialogFragment
import com.template.mvvm.base.ui.ViewModelFragment
import kotlin.reflect.KClass

fun androidx.fragment.app.Fragment.getDimensionPixel(@DimenRes res: Int) = resources.getDimensionPixelSize(res)
fun androidx.fragment.app.Fragment.getDimension(@DimenRes res: Int) = resources.getDimension(res)

@Size
fun androidx.fragment.app.Fragment.getScreenSize() = com.template.mvvm.base.utils.getScreenSize(context, 0)

fun androidx.fragment.app.Fragment.addFragmentToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
    childFragmentManager.transact {
        add(fragment, tag)
    }
}

fun androidx.fragment.app.Fragment.replaceFragmentToFragment(fragment: androidx.fragment.app.Fragment, frameId: Int) {
    childFragmentManager.transact {
        replace(frameId, fragment)
    }
}

fun androidx.fragment.app.FragmentManager.transact(action: androidx.fragment.app.FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun <FRG : androidx.fragment.app.Fragment> KClass<FRG>.newInstance(
    context: Context?,
    args: Bundle? = EMPTY
) =
    context?.run {
        (androidx.fragment.app.Fragment.instantiate(
            context,
            java.name
        ) as FRG).apply { arguments = args }
    } ?: kotlin.run { throw NullPointerException("[Context] cannot be null.") }

inline fun <reified FRG : ViewModelFragment<VM>, reified VM : ViewModel> KClass<FRG>.newInstance(
    context: Context?,
    args: Bundle? = EMPTY
): ViewModelFragment<VM> =
    context?.run {
        (androidx.fragment.app.Fragment.instantiate(
            context,
            java.name
        ) as ViewModelFragment<VM>).apply {
            arguments = args
            if (arguments == EMPTY) {
                arguments = Bundle()
            }
            arguments?.putSerializable("vm", VM::class.java)
        }
    } ?: kotlin.run { throw NullPointerException("[Context] cannot be null.") }

inline fun <reified FRG : ViewModelDialogFragment<VM>, reified VM : ViewModel> KClass<FRG>.newInstance(
    context: Context?,
    args: Bundle? = EMPTY
): ViewModelDialogFragment<VM> =
    context?.run {
        (androidx.fragment.app.Fragment.instantiate(
            context,
            java.name
        ) as ViewModelDialogFragment<VM>).apply {
            arguments = args
            if (arguments == EMPTY) {
                arguments = Bundle()
            }
            arguments?.putSerializable("vm", VM::class.java)
        }
    } ?: kotlin.run { throw NullPointerException("[Context] cannot be null.") }