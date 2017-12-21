package com.template.mvvm.app.binding

import android.arch.lifecycle.ViewModel

interface OnCommandListener {
    fun onCommand(id: Int)
}

interface OnItemCommandListener {
    fun onCommand(vm: ViewModel)
}

interface OnReloadListener {
    fun onReload()
}

