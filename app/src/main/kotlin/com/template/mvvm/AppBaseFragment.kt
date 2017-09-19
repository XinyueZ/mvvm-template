package com.template.mvvm

import android.arch.lifecycle.AndroidViewModel
import com.template.mvvm.ui.LifeFragment

abstract class AppBaseFragment<out T : AndroidViewModel>: LifeFragment<T>()