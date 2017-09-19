package com.template.mvvm

import android.arch.lifecycle.ViewModel
import com.template.mvvm.ui.LifeFragment

abstract class AppBaseFragment<out T : ViewModel>: LifeFragment<T>()