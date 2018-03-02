package com.template.mvvm.app.splash

import android.view.View
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.FragmentSplashBinding
import com.template.mvvm.base.ui.ViewModelFragment
import com.template.mvvm.core.get
import com.template.mvvm.core.models.registerLifecycleOwner
import com.template.mvvm.core.models.splash.SplashViewModel

class SplashFragment : ViewModelFragment<SplashViewModel>() {

    override fun onViewCreated(view: View) = FragmentSplashBinding.bind(view).apply {
        requestViewModel().get(this@SplashFragment) {
            vm = this
            registerLifecycleOwner(this@SplashFragment)
        }
    }

    override fun getLayout() = R.layout.fragment_splash
}