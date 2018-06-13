/*
 * Copyright 2017, The Android Open Source Project
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
package com.template.mvvm.base.ext.android.view

import android.app.Activity
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.template.mvvm.base.ext.android.app.getScreenSize
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.channels.consumeEach

fun View.showSnackbar(snackbarText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, snackbarText, timeLength).show()
}

fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    liveData: LiveData<String>, timeLength: Int = Snackbar.LENGTH_SHORT
) {
    liveData.observe(lifecycleOwner, Observer {
        it?.let { showSnackbar(it, timeLength) }
    })
}

fun View.onClick(block: suspend () -> Unit) {
    val eventActor = actor<Unit>(UI, capacity = Channel.CONFLATED) {
        // Handling only most recently received update.
        consumeEach { block() }
    }
    setOnClickListener {
        eventActor.offer(Unit)
    }
}

fun View.setUpGoldenRatioWidth(activity: Activity): View? {
    layoutParams.width = getUpGoldenRatioWidth(activity)
    return this
}

fun View.setUpGoldenRatioInvertedWidth(activity: Activity): View? {
    layoutParams.width = getUpGoldenRatioInvertedWidth(activity)
    return this
}

fun View.setUpGoldenRatioHeight(activity: Activity): View? {
    layoutParams.height = getUpGoldenRatioHeight(activity)
    return this
}

fun View.setUpGoldenRatioInvertedHeight(activity: Activity): View? {
    layoutParams.height = getUpGoldenRatioInvertedHeight(activity)
    return this
}

fun View.getUpGoldenRatioWidth(activity: Activity) =
    (activity.getScreenSize().width.toFloat() * 0.618339887f).toInt()

fun View.getUpGoldenRatioInvertedWidth(activity: Activity) =
    (activity.getScreenSize().width.toFloat() * (1f - 0.618339887f)).toInt()

fun View.getUpGoldenRatioHeight(activity: Activity) =
    (activity.getScreenSize().height.toFloat() * 0.618339887f).toInt()

fun View.getUpGoldenRatioInvertedHeight(activity: Activity) =
    (activity.getScreenSize().height.toFloat() * (1f - 0.618339887f)).toInt()

