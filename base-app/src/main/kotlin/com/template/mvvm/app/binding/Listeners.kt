package com.template.mvvm.app.binding

import androidx.lifecycle.ViewModel

interface OnCommandListener {
    fun onCommand(id: Int)
}

interface OnItemCommandListener {
    fun onCommand(vm: ViewModel, shared: Any?)
}

interface OnReloadListener {
    fun onReload()
}

