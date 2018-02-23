package com.template.mvvm.app.splash

import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentSplashBinding
import com.template.mvvm.base.ui.LiveFragment
import com.template.mvvm.core.generateViewModel
import com.template.mvvm.core.models.registerLifecycleOwner
import com.template.mvvm.core.models.splash.SplashViewModel

class SplashFragment : LiveFragment() {

    override fun onViewCreated(view: View) = FragmentSplashBinding.bind(view).apply {
        SplashViewModel::class.generateViewModel(this@SplashFragment) {
            vm = this
            registerLifecycleOwner(this@SplashFragment)
        }
    }

    override fun getLayout() = R.layout.fragment_splash
}