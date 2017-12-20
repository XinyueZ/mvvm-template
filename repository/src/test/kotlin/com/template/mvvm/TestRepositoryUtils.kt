package com.template.mvvm

import android.content.Context
import org.robolectric.shadows.ShadowApplication

fun context(): Context = ShadowApplication.getInstance().applicationContext



