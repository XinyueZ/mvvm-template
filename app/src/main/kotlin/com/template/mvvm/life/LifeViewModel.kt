package com.template.mvvm.life

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleRegistryOwner

abstract class LifeViewModel(context: Application) : AndroidViewModel(context) {
    open fun registerLifecycleOwner(lifecycleRegistryOwner: LifecycleRegistryOwner) = true
}