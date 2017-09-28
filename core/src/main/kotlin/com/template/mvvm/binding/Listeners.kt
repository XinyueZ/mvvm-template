package com.template.mvvm.binding

import android.arch.lifecycle.ViewModel

interface OnCommandListener {
    fun onCommand(id: Int)
}

interface OnIndicatorClickListener {
    fun onIndicatorClick()
}

interface OnItemCommandListener {
    fun onCommand(vm: ViewModel)
}

