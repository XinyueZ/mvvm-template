package com.template.mvvm.core

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI

object ViewModelInjection {
    var bgContext = CommonPool
    var uiContext = UI
}