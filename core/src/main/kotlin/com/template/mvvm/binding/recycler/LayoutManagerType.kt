package com.template.mvvm.binding.recycler

import android.support.annotation.IntDef

@IntDef(LINEAR_VERTICAL, LINEAR_VERTICAL_REVERSE)
@Retention(AnnotationRetention.SOURCE)
annotation class LayoutManagerType

const val LINEAR_VERTICAL = 0L
const val LINEAR_VERTICAL_REVERSE = 1L