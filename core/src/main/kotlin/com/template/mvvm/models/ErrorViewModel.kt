package com.template.mvvm.models

import android.support.annotation.StringRes
import com.template.mvvm.arch.SingleLiveData

class ErrorViewModel : SingleLiveData<Error>() {
    val t: Throwable? = value?.t
    @StringRes
    val wording: Int? = value?.wording
    @StringRes
    val retryWording: Int? = value?.retryWording
    val retry: (() -> Unit)? = value?.retry
}

class Error(val t: Throwable, @StringRes val wording: Int, @StringRes val retryWording: Int, val retry: () -> Unit) : AbstractViewModel()