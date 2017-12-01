package com.template.mvvm.binding

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

interface OnListItemBoundListener {
    fun onBound(position: Int)
}

