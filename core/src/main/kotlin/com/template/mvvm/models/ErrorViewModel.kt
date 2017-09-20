package com.template.mvvm.models

import android.support.annotation.StringRes

class ErrorViewModel(val t: Throwable, @StringRes val wording: Int, @StringRes val retryWording: Int, val retry: () -> Unit) : AbstractViewModel()